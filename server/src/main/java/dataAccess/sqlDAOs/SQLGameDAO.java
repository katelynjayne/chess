package dataAccess.sqlDAOs;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {

   public SQLGameDAO() throws DataAccessException {
      DatabaseManager.createDatabase();
   }
   public void clear() throws DataAccessException {
      String statement = "DELETE FROM game";
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            ps.executeUpdate();
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
   }

   public Collection<GameData> listGames() throws DataAccessException {
      Collection<GameData> games = new ArrayList<>();
      Gson serializer = new Gson();
      String statement = "SELECT * FROM game";
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            try (var rs = ps.executeQuery()) {
               while (rs.next()) {
                  int id = rs.getInt("id");
                  String whiteUsername = rs.getString("whiteUsername");
                  String blackUsername = rs.getString("blackUsername");
                  String gameName = rs.getString("gameName");
                  ChessGame game = serializer.fromJson(rs.getString("gameData"), ChessGame.class);
                  GameData gameData = new GameData(id,whiteUsername,blackUsername,gameName,game);
                  games.add(gameData);
               }
            }
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
      return games;
   }

   public int createGame(String gameName) throws DataAccessException {
      ChessGame gameData = new ChessGame();
      Gson serializer = new Gson();
      int id;
      String statement = "INSERT INTO game (gameName, gameData) values (?,?)";
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, gameName);
            ps.setString(2, serializer.toJson(gameData));
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
               if (generatedKeys.next()) {
                  id = generatedKeys.getInt(1);
               }
               else {
                  throw new SQLException("Insertion failed, no ID obtained.");
               }
            }
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
      return id;
   }

   public GameData getGame(int gameID) throws DataAccessException{
      Gson serializer = new Gson();
      String statement = "SELECT id,whiteUsername,blackUsername,gameName,gameData FROM game WHERE id=?";
      GameData gameData;
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            ps.setInt(1,gameID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
               int id = rs.getInt("id");
               String whiteUsername = rs.getString("whiteUsername");
               String blackUsername = rs.getString("blackUsername");
               String gameName = rs.getString("gameName");
               ChessGame game = serializer.fromJson(rs.getString("gameData"), ChessGame.class);
               gameData = new GameData(id,whiteUsername,blackUsername,gameName,game);
            }
            else {
               return null;
            }
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
      return gameData;
   }

   public boolean updateGame(ChessGame.TeamColor color, String username, GameData game) throws DataAccessException {
      String statement;
      if (color == ChessGame.TeamColor.WHITE && game.whiteUsername() == null) {
         statement = "UPDATE game SET whiteUsername=? WHERE id=?";
      }
      else if (color == ChessGame.TeamColor.BLACK && game.blackUsername() == null){
         statement = "UPDATE game SET blackUsername=? WHERE id=?";
      }
      else {
         return false;
      }
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            ps.setString(1,username);
            ps.setInt(2,game.gameID());
            ps.executeUpdate();
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
      return true;
   }
}

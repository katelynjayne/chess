package dataAccess.sqlDAOs;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAO;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
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

   public Collection<GameData> listGames() {
      //we gonna make a list of EVERYTHING in the table
      return null;
   }

   public int createGame(String gameName) {
      //add a row
      //return id in row (how to access?)
      return 0;
   }

   public GameData getGame(int gameID) {
      //return game data row with matching ID
      return null;
   }

   public boolean updateGame(ChessGame.TeamColor color, String username, GameData game) {
      //this is a function in GameData...
      //may need to refactor?
      return game.setUsername(color, username);
   }
}

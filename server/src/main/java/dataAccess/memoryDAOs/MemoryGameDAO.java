package dataAccess.memoryDAOs;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
   private final Collection<GameData> games = new ArrayList<>();
   private int counter = 0;

   public void clear() {
      games.clear();
   }
   public Collection<GameData> listGames(){
      return games;
   }

   public int createGame(String gameName) {
      GameData game = new GameData(counter, null, null, gameName, new ChessGame());
      counter += 1;
      games.add(game);
      return game.gameID();
   }

   public GameData getGame(int gameID) {
      for (GameData game: games) {
         if (game.gameID() == gameID) {
            return game;
         }
      }
      return null;
   }

   public boolean updateGame(ChessGame.TeamColor color, String username, GameData game) {
      //this is broken :)
      return false;
   }

   public void updateBoard(String gameData, int gameID) throws DataAccessException {}
   public void leaveGame(ChessGame.TeamColor color, GameData game) throws DataAccessException {}
}

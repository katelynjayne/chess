package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
   private Collection<GameData> games = new ArrayList<>();
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
}

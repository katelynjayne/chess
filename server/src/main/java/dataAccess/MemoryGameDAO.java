package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{
   private Collection<GameData> games = new ArrayList<>();

   public void clear() {
      games.clear();
   }
   public Collection<GameData> listGames(){
      return games;
   }

   public int createGame(GameData game) {
      games.add(game);
      return game.gameID();
   }
}

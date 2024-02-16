package dataAccess;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.Collection;

public class GameDAO {
   private Collection<ChessGame> games = new ArrayList<>();

   public void clear() {
      games.clear();
   }
}

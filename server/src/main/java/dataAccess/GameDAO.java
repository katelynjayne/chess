package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameDAO {
   private Collection<GameData> games = new ArrayList<>();

   public void clear() {
      games.clear();
   }
}

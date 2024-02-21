package dataAccess;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{
   private Collection<GameData> games = new ArrayList<>();

   public void clear() {
      games.clear();
   }
}

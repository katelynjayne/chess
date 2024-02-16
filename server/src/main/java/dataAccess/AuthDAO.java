package dataAccess;

import java.util.ArrayList;
import java.util.Collection;

public class AuthDAO {
   private Collection<String> authTokens = new ArrayList<>();

   public void clear() {
      authTokens.clear();
   }
}

package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
   private Collection<AuthData> authTokens = new ArrayList<>();

   public void clear() {
      authTokens.clear();
   }

   public AuthData createAuth(String username) {
      String token = UUID.randomUUID().toString(); //generates auth token
      AuthData auth = new AuthData(token, username);
      authTokens.add(auth);
      return auth;
   }
}

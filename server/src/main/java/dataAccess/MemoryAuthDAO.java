package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
   private final Collection<AuthData> authTokens = new ArrayList<>();

   public void clear() {
      authTokens.clear();
   }

   public AuthData createAuth(String username) {
      String token = UUID.randomUUID().toString(); //generates auth token
      AuthData auth = new AuthData(token, username);
      authTokens.add(auth);
      return auth;
   }

   public AuthData getAuth(String token) {
      for (AuthData auth:authTokens) {
         if (Objects.equals(auth.authToken(), token)) {
            return auth;
         }
      }
      return null;
   }

   public void deleteAuth(AuthData auth) {
      authTokens.remove(auth);
   }
}

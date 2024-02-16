package dataAccess;

import java.util.HashMap;

public class UserDAO {
   private HashMap<String, String> users = new HashMap<String, String>();
   public void clear() {
      users.clear();
   }

   public String getUser(String username) {
      if (users.containsKey(username)) {
         return users.get(username); //returns password.
         // find way to return both username and password??
      }
      return null;
   }

   public void createUser(String username, String password) {
      users.put(username, password);
   }
}

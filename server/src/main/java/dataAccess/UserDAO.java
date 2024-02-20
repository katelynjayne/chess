package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class UserDAO {
   private ArrayList<UserData> users = new ArrayList<>();
   public void clear() {
      users.clear();
   }

   public UserData getUser(String username) {
      for (UserData user:users) {
         if (Objects.equals(user.username(), username)) {
            return user;
         }
      }
      return null;
   }

   public void createUser(String username, String password, String email) {
      UserData user = new UserData(username, password, email);
      users.add(user);
   }

   public void insertUser(UserData user) {
      users.add(user);
   }
}

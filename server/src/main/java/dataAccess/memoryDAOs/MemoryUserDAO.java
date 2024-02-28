package dataAccess.memoryDAOs;

import dataAccess.UserDAO;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
   private final ArrayList<UserData> users = new ArrayList<>();
   public void clear() {
      users.clear();
   }

   public UserData getUser(String username) {
      for (UserData user : users) {
         if (Objects.equals(user.username(), username)) {
            return user;
         }
      }
      return null;
   }

   public void insertUser(UserData user) {
      users.add(user);
   }
}

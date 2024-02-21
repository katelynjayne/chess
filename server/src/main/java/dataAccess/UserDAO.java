package dataAccess;

import model.UserData;

public interface UserDAO {
   void clear();
   UserData getUser(String username);
   void insertUser(UserData user);
}

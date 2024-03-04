package dataAccess.sqlDAOs;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDAO;
import model.UserData;

public class SQLUserDAO implements UserDAO {

   public SQLUserDAO() throws DataAccessException {
      DatabaseManager.createDatabase();
   }

   public void clear() {
      //clear the table
   }

   public UserData getUser(String username) {
      // find row of table in column username that matches argument
      // un hash password, make UserData obj, and return
      return null;
   }

   public void insertUser(UserData user) {
      //uh oh we need to refactor this so instead of making a user object, we pass in the 3 things
      //actually instead we can just access the 3 things with the getters so i think it's chill
   }
}

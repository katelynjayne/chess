package dataAccess;

import model.UserData;

public interface UserDAO {
   void clear() throws DataAccessException;
   UserData getUser(String username) throws DataAccessException;
   void insertUser(UserData user) throws DataAccessException;
}

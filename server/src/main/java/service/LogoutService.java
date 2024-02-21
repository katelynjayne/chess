package service;

import dataAccess.DataAccessException;
import model.AuthData;

public class LogoutService extends Service {
   public void logout(String token) throws DataAccessException {
      AuthData db_auth = authDAO.getAuth(token);
      if (db_auth == null) {
         throw new DataAccessException("Error: unauthorized");
      }
      authDAO.deleteAuth(db_auth);
   }
}

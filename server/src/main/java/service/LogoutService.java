package service;

import dataAccess.DataAccessException;
import model.AuthData;

public class LogoutService extends Service {
   public LogoutService() throws DataAccessException {
   }

   public void logout(String token) throws DataAccessException {
      checkAuth(token);
      authDAO.deleteAuth(authDAO.getAuth(token));
   }
}

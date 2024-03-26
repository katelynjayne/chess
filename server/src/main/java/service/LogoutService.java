package service;

import dataAccess.DataAccessException;

public class LogoutService extends Service {
   public LogoutService() throws DataAccessException {
   }

   public void logout(String token) throws DataAccessException {
      checkAuth(token);
      authDAO.deleteAuth(authDAO.getAuth(token));
   }
}

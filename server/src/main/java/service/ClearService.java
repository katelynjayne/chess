package service;

import dataAccess.DataAccessException;

public class ClearService extends Service  {

   public ClearService() throws DataAccessException {
   }

   public void clear() {
      userDAO.clear();
      authDAO.clear();
      gameDAO.clear();
   }
}

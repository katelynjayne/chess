package service;

import dataAccess.DataAccessException;

public class ClearService extends Service  {

   public ClearService() throws DataAccessException {
   }

   public void clear() throws DataAccessException {
      authDAO.clear();
      userDAO.clear();
      gameDAO.clear();
   }
}

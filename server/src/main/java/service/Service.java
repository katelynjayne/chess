package service;

import dataAccess.*;
import dataAccess.memoryDAOs.MemoryAuthDAO;
import dataAccess.memoryDAOs.MemoryGameDAO;
import dataAccess.memoryDAOs.MemoryUserDAO;
import dataAccess.sqlDAOs.SQLAuthDAO;
import dataAccess.sqlDAOs.SQLGameDAO;
import dataAccess.sqlDAOs.SQLUserDAO;
import model.AuthData;

public class Service {
   protected static SQLUserDAO userDAO;
   protected static SQLAuthDAO authDAO;
   protected static SQLGameDAO gameDAO;

   public Service() throws DataAccessException {
      userDAO = new SQLUserDAO();
      authDAO = new SQLAuthDAO();
      gameDAO = new SQLGameDAO();
   }

   protected static AuthData checkAuth(String authToken) throws DataAccessException{
      AuthData auth = authDAO.getAuth(authToken);
      if (auth == null) {
         throw new DataAccessException("Error: unauthorized",401);
      }
      return auth;
   }
}

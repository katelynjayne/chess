package service;

import dataAccess.*;
import dataAccess.memoryDAOs.MemoryAuthDAO;
import dataAccess.memoryDAOs.MemoryGameDAO;
import dataAccess.memoryDAOs.MemoryUserDAO;
import model.AuthData;

public class Service {
   protected static MemoryUserDAO userDAO = new MemoryUserDAO();
   protected static MemoryAuthDAO authDAO = new MemoryAuthDAO();
   protected static MemoryGameDAO gameDAO = new MemoryGameDAO();

   protected static AuthData checkAuth(String authToken) throws DataAccessException{
      AuthData auth = authDAO.getAuth(authToken);
      if (auth == null) {
         throw new DataAccessException("Error: unauthorized");
      }
      return auth;
   }
}

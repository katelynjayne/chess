package service;

import dataAccess.*;

public class Service {
   protected static MemoryUserDAO userDAO = new MemoryUserDAO();
   protected static MemoryAuthDAO authDAO = new MemoryAuthDAO();
   protected static MemoryGameDAO gameDAO = new MemoryGameDAO();

   protected static void checkAuth(String authToken) throws DataAccessException{
      if (authDAO.getAuth(authToken) == null) {
         throw new DataAccessException("Error: unauthorized");
      }
   }
}

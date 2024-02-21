package service;

import dataAccess.*;

public class Service {
   protected static MemoryUserDAO userDAO = new MemoryUserDAO();
   protected static MemoryAuthDAO authDAO = new MemoryAuthDAO();
   protected static MemoryGameDAO gameDAO = new MemoryGameDAO();
}

package service;

import dataAccess.*;

public class Service {
   protected static UserDAO userDAO = new UserDAO();
   protected static AuthDAO authDAO = new AuthDAO();
   protected static GameDAO gameDAO = new GameDAO();
}

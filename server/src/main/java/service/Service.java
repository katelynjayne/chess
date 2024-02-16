package service;

import dataAccess.*;

public class Service {
   protected UserDAO userDAO = new UserDAO();
   protected AuthDAO authDAO = new AuthDAO();
   protected GameDAO gameDAO = new GameDAO();
}

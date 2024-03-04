package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class RegisterService extends Service {
   public RegisterService() throws DataAccessException {
   }

   public AuthData register(UserData user) throws DataAccessException {
      if (userDAO.getUser(user.username()) == null) {
         userDAO.insertUser(user);
         return authDAO.createAuth(user.username());
      }
      else {
         throw new DataAccessException("Error: already taken");
      }
   }
}


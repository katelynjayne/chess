package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class LoginService extends Service{
   public AuthData login(UserData user) throws DataAccessException {
      UserData dbUser = userDAO.getUser(user.username());
      if (dbUser == null || !Objects.equals(user.password(), dbUser.password())) {
         throw new DataAccessException("Error: unauthorized");
      }
      return authDAO.createAuth(user.username());
   }
}

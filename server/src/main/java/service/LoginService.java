package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class LoginService extends Service{
   public AuthData login(UserData user) throws DataAccessException {
      UserData db_user = userDAO.getUser(user.username());
      if (db_user == null || !Objects.equals(user.password(), db_user.password())) {
         throw new DataAccessException("Error: unauthorized");
      }
      return authDAO.createAuth(user.username());
   }
}

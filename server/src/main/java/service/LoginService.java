package service;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class LoginService extends Service{
   public LoginService() throws DataAccessException {
   }

   public AuthData login(UserData user) throws DataAccessException {
      UserData dbUser = userDAO.getUser(user.username());
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      if (dbUser == null || !encoder.matches(user.password(), dbUser.password())) {
         throw new DataAccessException("Error: unauthorized",401);
      }
      return authDAO.createAuth(user.username());
   }
}

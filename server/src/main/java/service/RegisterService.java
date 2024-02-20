package service;

import model.AuthData;
import model.UserData;

public class RegisterService extends Service {
   public AuthData register(UserData user) {
      if (userDAO.getUser(user.username()) == null) { //error if otherwise!
         userDAO.insertUser(user);
         return authDAO.createAuth(user.username());
      }
      return new AuthData("1234","hey");
   }
}


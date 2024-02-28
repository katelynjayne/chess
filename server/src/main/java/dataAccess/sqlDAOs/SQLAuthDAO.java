package dataAccess.sqlDAOs;

import dataAccess.AuthDAO;
import model.AuthData;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

   public void clear() {
      //clear the table
   }

   public AuthData createAuth(String username) {
      String token = UUID.randomUUID().toString(); //generates auth token
      AuthData auth = new AuthData(token, username);
      //put it in the database
      return auth;
   }

   public AuthData getAuth(String token) {
      //find row of table in column token that matches this token
      //make AuthData object, return it
      return null;
   }

   public void deleteAuth(AuthData auth) {
      //remove that row!
   }
}

package dataAccess.sqlDAOs;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

   public SQLAuthDAO() throws DataAccessException {
      DatabaseManager.createDatabase();
   }

   public void clear() throws DataAccessException{
      String statement = "DELETE FROM auth";
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            ps.executeUpdate();
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
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

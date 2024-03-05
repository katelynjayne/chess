package dataAccess.sqlDAOs;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
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

   public AuthData createAuth(String username) throws DataAccessException {
      String token = UUID.randomUUID().toString(); //generates auth token
      AuthData auth = new AuthData(token, username);
      String statement = "INSERT INTO auth (token, username) values (?,?)";
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            ps.setString(1, token);
            ps.setString(2, username);
            ps.executeUpdate();
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
      return auth;
   }

   public AuthData getAuth(String token) throws DataAccessException {
      String statement = "SELECT token, id FROM auth WHERE token=?";
      String username;
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            username = rs.getString("username");
         }
      }
      catch (DataAccessException | SQLException e) {
         if (Objects.equals(e.getMessage(), "Illegal operation on empty result set.")) {
            return null;
         }
         else {
            throw new DataAccessException(e.getMessage(), 500);
         }
      }
      return new AuthData(token, username);
   }

   public void deleteAuth(AuthData auth) {
      //remove that row!
   }
}

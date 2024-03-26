package dataAccess.sqlDAOs;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDAO;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

   public SQLUserDAO() throws DataAccessException {
      DatabaseManager.createDatabase();
   }

   public void clear() throws DataAccessException {
      String statement = "DELETE FROM user";
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            ps.executeUpdate();
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
   }

   public UserData getUser(String username) throws DataAccessException {
      String statement = "SELECT password, email FROM user WHERE username=?";
      String password;
      String email;
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
               password = rs.getString("password");
               email = rs.getString("email");
            }
            else {
               return null;
            }
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
      return new UserData(username, password, email);
   }

   public void insertUser(UserData user) throws DataAccessException{
      String statement = "INSERT INTO user (username, password, email) values (?, ?, ?)";
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      String hashedPassword = encoder.encode(user.password());
      try (Connection conn = DatabaseManager.getConnection()) {
         try (var ps = conn.prepareStatement(statement)) {
            ps.setString(1, user.username());
            ps.setString(2, hashedPassword);
            ps.setString(3, user.email());
            ps.executeUpdate();
         }
      }
      catch (DataAccessException | SQLException e) {
         throw new DataAccessException(e.getMessage(), 500);
      }
   }
}

package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.sqlDAOs.SQLAuthDAO;
import dataAccess.sqlDAOs.SQLGameDAO;
import dataAccess.sqlDAOs.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataAccessTests {
   private SQLUserDAO userDAO;
   private SQLAuthDAO authDAO;
   private SQLGameDAO gameDAO;
   @BeforeEach
   public void setup () throws DataAccessException {
      userDAO = new SQLUserDAO();
      authDAO = new SQLAuthDAO();
      gameDAO = new SQLGameDAO();
   }


   @Test
   @Order(1)
   public void posCreateAuthTest() {

   }

   @Test
   @Order(2)
   public void negCreateAuthTest() {

   }

   @Test
   @Order(3)
   public void posGetAuthTest() {

   }

   @Test
   @Order(4)
   public void negGetAuthTest() {

   }

   @Test
   @Order(5)
   public void posDeleteAuthTest() {

   }

   @Test
   @Order(6)
   public void negDeleteAuthTest() {

   }

   @Test
   @Order(7)
   public void authClearTest() {
      Assertions.assertDoesNotThrow(() -> authDAO.clear());
   }

   @Test
   @Order(8)
   public void posInsertUserTest() {
      UserData user = new UserData("testuser", "password", "email");
      Assertions.assertDoesNotThrow(() -> userDAO.insertUser(user));
   }

   @Test
   @Order(9)
   public void negInsertUserTest() {
      UserData user = new UserData("testuser", "password", "email");
      Assertions.assertThrows(DataAccessException.class, () -> userDAO.insertUser(user));
   }

   @Test
   @Order(10)
   public void posGetUserTest() {

   }

   @Test
   @Order(11)
   public void negGetUserTest() {

   }

   @Test
   @Order(12)
   public void userClearTest() {
      Assertions.assertDoesNotThrow(() -> userDAO.clear());
   }
   @Test
   @Order(13)
   public void posCreateGameTest() {

   }

   @Test
   @Order(14)
   public void negCreateGameTest() {

   }

   @Test
   @Order(15)
   public void posGetGameTest() {

   }

   @Test
   @Order(16)
   public void negGetGameTest() {

   }

   @Test
   @Order(17)
   public void posUpdateGameTest() {

   }

   @Test
   @Order(18)
   public void negUpdateGameTest() {

   }
   @Test
   @Order(19)
   public void posListGamesTest() {

   }

   @Test
   @Order(20)
   public void negListGamesTest() {

   }
   @Test
   @Order(21)
   public void gameClearTest() {
      Assertions.assertDoesNotThrow(() -> gameDAO.clear());
   }
}
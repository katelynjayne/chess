package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.sqlDAOs.SQLAuthDAO;
import dataAccess.sqlDAOs.SQLGameDAO;
import dataAccess.sqlDAOs.SQLUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataAccessTests {
   private static SQLUserDAO userDAO;
   private static SQLAuthDAO authDAO;
   private static SQLGameDAO gameDAO;
   private static UserData user;

   @BeforeAll
   static void setUp() throws DataAccessException{
      userDAO = new SQLUserDAO();
      userDAO.clear();
      authDAO = new SQLAuthDAO();
      authDAO.clear();
      gameDAO = new SQLGameDAO();
      gameDAO.clear();
      user = new UserData("testuser", "password", "email");
   }

   @Test
   @Order(1)
   public void posInsertUserTest() {
      Assertions.assertDoesNotThrow(() -> userDAO.insertUser(user));
   }

   @Test
   @Order(2)
   public void negInsertUserTest() {
      Assertions.assertThrows(DataAccessException.class, () -> userDAO.insertUser(user));
   }

   @Test
   @Order(3)
   public void posGetUserTest() throws DataAccessException {
      Assertions.assertEquals("testuser", userDAO.getUser("testuser").username());
   }

   @Test
   @Order(4)
   public void negGetUserTest() throws DataAccessException {
      Assertions.assertNull(userDAO.getUser("baduser"));
   }
   @Test
   @Order(5)
   public void posCreateAuthTest() throws DataAccessException{
      String token = authDAO.createAuth("testuser").authToken();
      Assertions.assertNotNull(token);
   }

   @Test
   @Order(6)
   public void negCreateAuthTest() {
      Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth("baduser"));
   }

   @Test
   @Order(7)
   public void posGetAuthTest() throws DataAccessException {
      String token = authDAO.createAuth("testuser").authToken();
      Assertions.assertEquals("testuser", authDAO.getAuth(token).username());
   }

   @Test
   @Order(8)
   public void negGetAuthTest() throws DataAccessException{
      Assertions.assertNull(authDAO.getAuth(""));
   }

   @Test
   @Order(9)
   public void posDeleteAuthTest() throws DataAccessException {
      AuthData auth = authDAO.createAuth("testuser");
      Assertions.assertDoesNotThrow(()->authDAO.deleteAuth(auth));
   }

   @Test
   @Order(10)
   public void negDeleteAuthTest() {
      Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(new AuthData("","baduser")));
   }


   @Test
   @Order(11)
   public void posCreateGameTest() {
      Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game"));
   }

   @Test
   @Order(12)
   public void negCreateGameTest() {
      Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game"));
   }

   @Test
   @Order(13)
   public void posGetGameTest() throws DataAccessException{
      int gameID = gameDAO.createGame("game2");
      Assertions.assertNotNull(gameDAO.getGame(gameID));
   }

   @Test
   @Order(14)
   public void negGetGameTest() throws DataAccessException{
      int gameID = gameDAO.createGame("game3");
      Assertions.assertNull(gameDAO.getGame(gameID + 10));
   }

   @Test
   @Order(15)
   public void posUpdateGameTest() throws DataAccessException {
      int gameID = gameDAO.createGame("game4");
      GameData game = gameDAO.getGame(gameID);
      Assertions.assertTrue(gameDAO.updateGame(ChessGame.TeamColor.WHITE,"testuser", game));
   }

   @Test
   @Order(16)
   public void negUpdateGameTest() throws DataAccessException {
      int gameID = gameDAO.createGame("game5");
      GameData game = gameDAO.getGame(gameID);
      gameDAO.updateGame(ChessGame.TeamColor.WHITE,"testuser", game);
      game = gameDAO.getGame(gameID);
      Assertions.assertFalse(gameDAO.updateGame(ChessGame.TeamColor.WHITE, "otheruser", game));
   }
   @Test
   @Order(17)
   public void posListGamesTest() throws DataAccessException{
      Assertions.assertEquals(6, gameDAO.listGames().size());
   }

   @Test
   @Order(18)
   public void negListGamesTest() throws DataAccessException {
      Assertions.assertNotEquals(0, gameDAO.listGames().size());
   }
   @Test
   @Order(19)
   public void authClearTest() {
      Assertions.assertDoesNotThrow(() -> authDAO.clear());
   }

   @Test
   @Order(20)
   public void userClearTest() {
      Assertions.assertDoesNotThrow(() -> userDAO.clear());
   }
   @Test
   @Order(21)
   public void gameClearTest() {
      Assertions.assertDoesNotThrow(() -> gameDAO.clear());
   }
}
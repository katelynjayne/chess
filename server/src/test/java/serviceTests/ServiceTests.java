package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import service.*;
import model.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {
   private RegisterService registerService;
   private LoginService loginService;
   private LogoutService logoutService;
   private ClearService clearService;
   private CreateGameService createGameService;
   private JoinGameService joinGameService;
   private ListGamesService listGamesService;
   private UserData user1;
   private UserData user2;
   private String authToken;

   @BeforeEach
   public void setup() {
      registerService = new RegisterService();
      loginService = new LoginService();
      logoutService = new LogoutService();
      clearService = new ClearService();
      createGameService = new CreateGameService();
      joinGameService = new JoinGameService();
      listGamesService = new ListGamesService();
      user1 = new UserData("username", "1234", "mail@mail.com");
      user2 = new UserData("username", "incorrect", "mail@mail.com");
   }

   @Test
   @Order(1)
   public void registerTest() throws DataAccessException {
      AuthData user1Auth = registerService.register(user1);
      assertEquals("username", user1Auth.username());
      assertThrows(DataAccessException.class, () -> registerService.register(user2));
   }

   @Test
   @Order(2)
   public void loginTest() throws DataAccessException {
      AuthData auth = loginService.login(user1);
      authToken = auth.authToken();
      assertEquals("username", auth.username());
      assertThrows(DataAccessException.class, () -> loginService.login(user2));
   }

   @Test
   @Order(3)
   public void logoutTest() throws DataAccessException {
      AuthData auth = loginService.login(user1);
      logoutService.logout(auth.authToken());
      assertThrows(DataAccessException.class, () -> logoutService.logout(auth.authToken()));
   }

   @Test
   @Order(4)
   public void createGameTest() throws DataAccessException{
      authToken = loginService.login(user1).authToken();
      int gameID = createGameService.createGame(authToken, "game1");
      assertEquals(0, gameID);
      int gameID2 = createGameService.createGame(authToken, "game2");
      assertEquals(1, gameID2);
      assertThrows(DataAccessException.class, () -> createGameService.createGame("", "badgame"));
   }

   @Test
   @Order(5)
   public void listGameTest() throws DataAccessException {
      authToken = loginService.login(user1).authToken();
      Collection<GameData> list = listGamesService.listGames(authToken);
      assertEquals(2, list.size());
      assertThrows(DataAccessException.class, () -> listGamesService.listGames(""));
   }

   @Test
   @Order(6)
   public void joinGameTest() throws DataAccessException {

   }

   @Test
   @Order(7)
   public void clearTest() throws DataAccessException {
      clearService.clear();
      assertThrows(DataAccessException.class, () -> loginService.login(user1));
   }
}

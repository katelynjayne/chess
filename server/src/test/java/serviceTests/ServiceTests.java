package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.*;
import service.*;
import model.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;

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
   public void posRegisterTest() throws DataAccessException {
      AuthData user1Auth = registerService.register(user1);
      assertEquals("username", user1Auth.username());
   }

   @Test
   @Order(2)
   public void negRegisterTest() {
      assertThrows(DataAccessException.class, () -> registerService.register(user2));
   }
   @Test
   @Order(3)
   public void posLoginTest() throws DataAccessException {
      AuthData auth = loginService.login(user1);
      authToken = auth.authToken();
      assertEquals("username", auth.username());
   }

   @Test
   @Order(4)
   public void negLoginTest(){
      assertThrows(DataAccessException.class, () -> loginService.login(user2));
   }

   @Test
   @Order(5)
   public void posLogoutTest() throws DataAccessException {
      AuthData auth = loginService.login(user1);
      logoutService.logout(auth.authToken());
      assertThrows(DataAccessException.class, () -> logoutService.logout(auth.authToken()));
   }

   @Test
   @Order(6)
   public void negLogoutTest() {
      assertThrows(DataAccessException.class, () -> logoutService.logout(""));
   }

   @Test
   @Order(7)
   public void posCreateGameTest() throws DataAccessException{
      authToken = loginService.login(user1).authToken();
      int gameID = createGameService.createGame(authToken, "game1");
      assertEquals(0, gameID);
      int gameID2 = createGameService.createGame(authToken, "game2");
      assertEquals(1, gameID2);
      assertThrows(DataAccessException.class, () -> createGameService.createGame("", "badgame"));
   }

   @Test
   @Order(8)
   public void negCreateGameTest() {
      assertThrows(DataAccessException.class, () -> createGameService.createGame("", "badgame"));
   }

   @Test
   @Order(9)
   public void posListGameTest() throws DataAccessException {
      authToken = loginService.login(user1).authToken();
      Collection<GameData> list = listGamesService.listGames(authToken);
      assertEquals(2, list.size());
      assertThrows(DataAccessException.class, () -> listGamesService.listGames(""));
   }

   @Test
   @Order(10)
   public void negListGameTest() {
      assertThrows(DataAccessException.class, () -> listGamesService.listGames(""));
   }

   @Test
   @Order(11)
   public void posJoinGameTest() throws DataAccessException {
      authToken = loginService.login(user1).authToken();
      joinGameService.joinGame(authToken, WHITE, 0);

      assertThrows(DataAccessException.class, () -> joinGameService.joinGame(authToken, WHITE, 0));
      GameData game = ((ArrayList<GameData>) listGamesService.listGames(authToken)).get(0);
      assertEquals(game, new GameData(0, "username", null, "game1", new ChessGame()));
   }

   @Test
   @Order(12)
   public void negJoinGameTests(){
      assertThrows(DataAccessException.class, () -> joinGameService.joinGame("", null, 0));
      assertThrows(DataAccessException.class, () -> joinGameService.joinGame(authToken, null, 3));
   }

   @Test
   @Order(13)
   public void clearTest() {
      clearService.clear();
      assertThrows(DataAccessException.class, () -> loginService.login(user1));
   }
}

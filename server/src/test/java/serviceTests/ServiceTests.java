package serviceTests;

import dataAccess.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;
import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
   public void registerTest() throws DataAccessException {
      AuthData user1Auth = registerService.register(user1);
      assertEquals("username", user1Auth.username());
      assertThrows(DataAccessException.class, () -> registerService.register(user2));
   }

   @Test
   public void loginTest() throws DataAccessException {
      registerService.register(user1);
      AuthData auth = loginService.login(user1);
      assertEquals("username", auth.username());
      assertThrows(DataAccessException.class, () -> loginService.login(user2));
   }

   @Test
   public void logoutTest() throws DataAccessException {
      registerService.register(user1);
      AuthData auth = loginService.login(user1);
      logoutService.logout(auth.authToken());
      assertThrows(DataAccessException.class, () -> logoutService.logout(auth.authToken()));
   }

   @Test
   public void clearTest() throws DataAccessException {
      registerService.register(user1);
      loginService.login(user1);
      clearService.clear();
      assertThrows(DataAccessException.class, () -> loginService.login(user1));
   }

}

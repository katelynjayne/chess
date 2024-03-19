package clientTests;

import chess.ChessGame;
import client.ServerFacade;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import responseAndRequest.CreateGameResponse;
import server.Server;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static int port;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clear() {
        try {
            URL url = new URI("http://localhost:"+ port+"/db").toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("DELETE");
            http.connect();
            if (http.getResponseCode() != 200) {
                throw new Exception("huh?");
            }
        }
        catch (Exception e) {
            System.out.println("ERROR TO CLEAR: " + e.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void posRegisterTest() throws Exception {
        AuthData auth = facade.register("test-user","test-password", "test-email");
        Assertions.assertEquals(36, auth.authToken().length());
    }

    @Test
    public void negRegisterTest() throws Exception {
        facade.register("test-user","test-password", "test-email");
        Assertions.assertThrows(Exception.class, () -> facade.register("test-user","test-password", "test-email"));
    }

    @Test
    public void posLoginTest() throws Exception {
        facade.register("test-user","test-password", "test-email");
        AuthData auth = facade.login("test-user","test-password");
        Assertions.assertEquals(36, auth.authToken().length());
    }

    @Test
    public void negLoginTest() {
        Assertions.assertThrows(Exception.class, () -> facade.login("bad-user", "password"));
    }

    @Test
    public void posCreateTest() throws Exception {
        facade.register("test-user","test-password", "test-email");
        AuthData auth = facade.login("test-user","test-password");
        CreateGameResponse response = facade.createGame(auth.authToken(), "test-game");
        Assertions.assertNotNull(response);
    }

    @Test
    public void negCreateTest() {
        Assertions.assertThrows(Exception.class, () -> facade.createGame(null, "test-game"));
    }

    @Test
    public void posListTest() throws Exception {
        facade.register("test-user","test-password", "test-email");
        AuthData auth = facade.login("test-user","test-password");
        facade.createGame(auth.authToken(), "test-game");
        facade.createGame(auth.authToken(), "test-game-2");
        Collection<GameData> list = facade.listGames(auth.authToken()).getGames();
        Assertions.assertEquals(2, list.size());
    }

    @Test
    public void negListTest() {
        Assertions.assertThrows(Exception.class, () -> facade.listGames(null));
    }

    @Test
    public void posJoinTest() throws Exception {
        facade.register("test-user","test-password", "test-email");
        AuthData auth = facade.login("test-user","test-password");
        int id = facade.createGame(auth.authToken(), "test-game").getGameID();
        Assertions.assertDoesNotThrow(() -> facade.joinGame(auth.authToken(), ChessGame.TeamColor.WHITE, id));
    }

    @Test
    public void negJoinTest() {
        Assertions.assertThrows(Exception.class, () -> facade.joinGame(null, null, 0));
    }

    @Test
    public void posLogoutTest() throws Exception{
        facade.register("test-user","test-password", "test-email");
        AuthData auth = facade.login("test-user","test-password");
        Assertions.assertDoesNotThrow(() -> facade.logout(auth.authToken()));
    }

    @Test
    public void negLogoutTest() {
        Assertions.assertThrows(Exception.class,() -> facade.logout(null));
    }

}

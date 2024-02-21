package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.ClearService;
import service.LoginService;
import service.LogoutService;
import service.RegisterService;
import spark.*;

import java.nio.file.Paths;

public class Server {

    public static void main(String[] args) {
        var obj = new Server();
        obj.run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) {
        RegisterService service = new RegisterService();
        Gson serializer = new Gson();
        try {
            UserData user = serializer.fromJson(req.body(), UserData.class);
            if (user.username() == null || user.password() == null || user.email() == null) {
                res.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            }
            AuthData authToken = service.register(user);
            res.status(200);
            return serializer.toJson(authToken);
        }
        catch (DataAccessException e) {
            res.status(403);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
            //return serializer.toJson(e);
            //this is ugly!!
        }
        catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }

    }

    private Object login(Request req, Response res) {
        LoginService service = new LoginService();
        Gson serializer = new Gson();
        try {
            AuthData auth = service.login(serializer.fromJson(req.body(), UserData.class));
            res.status(200);
            return serializer.toJson(auth);
        }
        catch (DataAccessException e) {
            res.status(401);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
        catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }
    private Object logout(Request req, Response res) {
        LogoutService service = new LogoutService();
        Gson serializer = new Gson();
        try {
            service.logout(req.headers("authorization"));
            res.status(200);
            return "{}";
        }
        catch (DataAccessException e) {
            res.status(401);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
        catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"" + e.getMessage() + "\" }";
        }
    }

    private Object listGames(Request req, Response res) {
        return 200;
    }

    private Object createGame(Request req, Response res) {
        return 200;
    }

    private Object joinGame(Request req, Response res) {
        return 200;
    }
    private Object clear(Request req, Response res) {
        try {
            ClearService service = new ClearService();
            service.clear();
            res.status(200);
            return "{}";
        }
        catch (Exception e){
            res.status(500);
            return new Gson().toJson(e);
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

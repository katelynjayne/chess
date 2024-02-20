package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.ClearService;
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
        UserData user = serializer.fromJson(req.body(), UserData.class);
        AuthData authToken = service.register(user);
        res.status(200);
        return serializer.toJson(authToken);
    }

    private Object login(Request req, Response res) {
        return 200;
    }
    private Object logout(Request req, Response res) {
        return 200;
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
            return ""; //expected json??
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

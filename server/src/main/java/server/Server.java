package server;

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
        return 200;
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
        return 200;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.*;
import spark.Request;
import spark.Response;

import java.util.Collection;
import java.util.Objects;

public class Handler {
   public String register(Request req, Response res) {
      RegisterService service = new RegisterService();
      Gson serializer = new Gson();
      try {
         UserData user = serializer.fromJson(req.body(), UserData.class);
         if (user.username() == null || user.password() == null || user.email() == null) {
            res.status(400);
            return serializer.toJson(new ExceptionResponse("Error: bad request"));
         }
         AuthData authToken = service.register(user);
         res.status(200);
         return serializer.toJson(authToken);
      }
      catch (DataAccessException e) {
         res.status(403);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
      catch (Exception e) {
         res.status(500);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }

   }

   public String login(Request req, Response res) {
      LoginService service = new LoginService();
      Gson serializer = new Gson();
      try {
         AuthData auth = service.login(serializer.fromJson(req.body(), UserData.class));
         res.status(200);
         return serializer.toJson(auth);
      }
      catch (DataAccessException e) {
         res.status(401);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
      catch (Exception e) {
         res.status(500);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
   }
   public String logout(Request req, Response res) {
      LogoutService service = new LogoutService();
      Gson serializer = new Gson();
      try {
         service.logout(req.headers("authorization"));
         res.status(200);
         return "{}";
      }
      catch (DataAccessException e) {
         res.status(401);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
      catch (Exception e) {
         res.status(500);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
   }

   public String listGames(Request req, Response res) {
      ListGamesService service = new ListGamesService();
      Gson serializer = new Gson();
      try {
         Collection<GameData> gameList = service.listGames(req.headers("authorization"));
         res.status(200);
         return serializer.toJson(new ListGamesResponse(gameList));
      }
      catch (DataAccessException e) {
         res.status(401);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
      catch (Exception e) {
         res.status(500);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
   }

   public String createGame(Request req, Response res) {
      CreateGameService service = new CreateGameService();
      Gson serializer = new Gson();
      try {
         CreateGameRequest input = serializer.fromJson(req.body(), CreateGameRequest.class);
         if (input.getGameName() == null) {
            res.status(400);
            return serializer.toJson(new ExceptionResponse("Error: bad request"));
         }
         int gameID = service.createGame(req.headers("authorization"), input.getGameName());
         res.status(200);
         return serializer.toJson(new CreateGameResponse(gameID));
      }
      catch (DataAccessException e) {
         res.status(401);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
      catch (Exception e) {
         res.status(500);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
   }

   public String joinGame(Request req, Response res) {
      JoinGameService service = new JoinGameService();
      Gson serializer = new Gson();
      try {
         JoinGameRequest input = serializer.fromJson(req.body(), JoinGameRequest.class);
         service.joinGame(req.headers("authorization"), input.playerColor(), input.gameID());
         res.status(200);
         return "{}";
      }
      catch (DataAccessException e) {
         int statusCode = 403;
         if (Objects.equals(e.getMessage(), "Error: unauthorized")) {
            statusCode = 401;
         }
         else if (Objects.equals(e.getMessage(), "Error: bad request")) {
            statusCode = 400;
         }
         res.status(statusCode);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
      catch (Exception e) {
         res.status(500);
         return serializer.toJson(new ExceptionResponse(e.getMessage()));
      }
   }
   public String clear(Request req, Response res) {
      try {
         ClearService service = new ClearService();
         service.clear();
         res.status(200);
         return "{}";
      }
      catch (Exception e){
         res.status(500);
         return new Gson().toJson(new ExceptionResponse(e.getMessage()));
      }
   }
}

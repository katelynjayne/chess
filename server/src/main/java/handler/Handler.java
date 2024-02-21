package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.*;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class Handler {
   public Object register(Request req, Response res) {
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

   public Object login(Request req, Response res) {
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
   public Object logout(Request req, Response res) {
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

   public Object listGames(Request req, Response res) {
      ListGamesService service = new ListGamesService();
      Gson serializer = new Gson();
      try {
         Collection<ChessGame> gameList = service.listGames(req.headers("authorization"));
         res.status(200);
         return serializer.toJson(gameList);
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

   public Object createGame(Request req, Response res) {
      CreateGameService service = new CreateGameService();
      Gson serializer = new Gson();
      try {
         service.createGame(req.headers("authorization"), null);
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

   public Object joinGame(Request req, Response res) {
      JoinGameService service = new JoinGameService();
      Gson serializer = new Gson();
      try {
         service.joinGame(req.headers("authorization"), null);
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
   public Object clear(Request req, Response res) {
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
}

package handler;

import chess.*;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.sqlDAOs.SQLAuthDAO;
import dataAccess.sqlDAOs.SQLGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WSHandler {
   Gson serializer = new Gson();
   GameDAO gameDAO;
   AuthDAO authDAO;
   @OnWebSocketMessage
   public void onMessage(Session session, String message) {
      try {
         UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
         switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(session, message);
            case JOIN_OBSERVER -> observe(session, message);
            case MAKE_MOVE -> move(session, message);
            case LEAVE -> leave(session, message);
            case RESIGN -> resign(session, message);
         }
      }
      catch (Exception e){
         try {
            ErrorMessage error = new ErrorMessage(e.getMessage());
            String json = serializer.toJson(error);
            session.getRemote().sendString(json);
         }
         catch (Exception err) {
            System.out.println(err.getMessage());
         }

      }

   }

   private void join(Session session, String json) throws IOException, DataAccessException {
      JoinPlayer command = serializer.fromJson(json, JoinPlayer.class);
      gameDAO = new SQLGameDAO();
      ChessGame game = gameDAO.getGame(command.getGameID()).game();
      LoadGame response = new LoadGame(game);
      String responseJson = serializer.toJson(response);
      session.getRemote().sendString(responseJson);
   }

   private void observe(Session session, String message) throws IOException, DataAccessException {
      JoinObserver command = serializer.fromJson(message, JoinObserver.class);
      gameDAO = new SQLGameDAO();
      ChessGame game = gameDAO.getGame(command.getGameID()).game();
      LoadGame response = new LoadGame(game);
      String json = serializer.toJson(response);
      session.getRemote().sendString(json);
   }

   private void move(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
      MakeMove command = serializer.fromJson(message, MakeMove.class);
      ChessGame game = gameDAO.getGame(command.getGameID()).game();
      authChecker(command.getAuthString(), game.getTeamTurn(), game.getBoard().getPiece(command.getMove().getStartPosition()), command.getGameID());
      game.makeMove(command.getMove());
      String boardData = serializer.toJson(game);
      gameDAO.updateBoard(boardData, command.getGameID());
      LoadGame response = new LoadGame(game);
      String json = serializer.toJson(response);
      session.getRemote().sendString(json);
   }

   private void authChecker(String authToken, ChessGame.TeamColor teamTurn, ChessPiece piece, int gameID) throws DataAccessException {
      if (piece == null) {
         throw new DataAccessException("There is no piece at that position.", 0);
      }
      authDAO = new SQLAuthDAO();
      String username = authDAO.getAuth(authToken).username();
      GameData game = gameDAO.getGame(gameID);
      if (!Objects.equals(username, game.whiteUsername()) && !Objects.equals(username, game.blackUsername())) {
         throw new DataAccessException("You are not playing this game!", 0);
      }
      if (Objects.equals(username, game.whiteUsername()) && teamTurn == ChessGame.TeamColor.BLACK
              || Objects.equals(username, game.blackUsername()) && teamTurn == ChessGame.TeamColor.WHITE) {
         throw new DataAccessException("It is not your turn.", 0);
      }
   }

   private void leave(Session session, String message) throws IOException {
      session.getRemote().sendString("RESPONSE!: you have left game");
   }

   private void resign(Session session, String message) throws IOException {
      session.getRemote().sendString("RESPONSE!: you lost womp womp");
   }
}

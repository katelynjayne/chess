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
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.*;

@WebSocket
public class WSHandler {
   Gson serializer = new Gson();
   GameDAO gameDAO;
   AuthDAO authDAO;

   private final Map<Session, String> sessions = new HashMap<>();
   private final Map<Integer, List<Session>> gameGroups = new HashMap<>();
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

   private void addToGroup(int gameID, Session session) {
      if (!gameGroups.containsKey(gameID)) {
         gameGroups.put(gameID, new ArrayList<>());
      }
      gameGroups.get(gameID).add(session);
   }

   private void broadcast(List<Session> sessions, String message) throws IOException {
      Notification notification = new Notification(message);
      String json = serializer.toJson(notification);
      for (Session session : sessions) {
         session.getRemote().sendString(json);
      }
   }

   private void join(Session session, String json) throws IOException, DataAccessException {
      JoinPlayer command = serializer.fromJson(json, JoinPlayer.class);
      authDAO = new SQLAuthDAO();
      String username = authDAO.getAuth(command.getAuthString()).username();
      sessions.put(session, command.getColorString());
      addToGroup(command.getGameID(), session);
      gameDAO = new SQLGameDAO();
      ChessGame game = gameDAO.getGame(command.getGameID()).game();
      LoadGame response = new LoadGame(game, command.getPlayerColor());
      String responseJson = serializer.toJson(response);
      session.getRemote().sendString(responseJson);
      broadcast(gameGroups.get(command.getGameID()), username + " has joined the game as " + command.getColorString());
   }

   private void observe(Session session, String message) throws IOException, DataAccessException {
      JoinObserver command = serializer.fromJson(message, JoinObserver.class);
      authDAO = new SQLAuthDAO();
      String username = authDAO.getAuth(command.getAuthString()).username();
      sessions.put(session, "observer");
      addToGroup(command.getGameID(), session);
      gameDAO = new SQLGameDAO();
      ChessGame game = gameDAO.getGame(command.getGameID()).game();
      LoadGame response = new LoadGame(game, ChessGame.TeamColor.WHITE);
      String json = serializer.toJson(response);
      session.getRemote().sendString(json);
      broadcast(gameGroups.get(command.getGameID()), username + " is now watching this game." );
   }

   private void move(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
      MakeMove command = serializer.fromJson(message, MakeMove.class);
      ChessGame game = gameDAO.getGame(command.getGameID()).game();
      authChecker(session, game.getBoard().getPiece(command.getMove().getStartPosition()));
      game.makeMove(command.getMove());
      String boardData = serializer.toJson(game);
      gameDAO.updateBoard(boardData, command.getGameID());

      for (Session allSessions : gameGroups.get(command.getGameID())) {
         ChessGame.TeamColor color = (Objects.equals(sessions.get(allSessions), "white")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
         LoadGame response = new LoadGame(game, color);
         String json = serializer.toJson(response);
         allSessions.getRemote().sendString(json);
      }
      broadcast(gameGroups.get(command.getGameID()), "A move has been made.");
   }

   private void authChecker(Session session, ChessPiece piece) throws DataAccessException {
      if (piece == null) {
         throw new DataAccessException("There is no piece at that location.", 0);
      }
      ChessGame.TeamColor pieceColor = piece.getTeamColor();
      if (Objects.equals(sessions.get(session), "observer"))  {
         throw new DataAccessException("You are not playing this game.", 0);
      }
      if (Objects.equals(sessions.get(session), "white") && pieceColor != ChessGame.TeamColor.WHITE
              || Objects.equals(sessions.get(session), "black") && pieceColor == ChessGame.TeamColor.WHITE) {
         throw new DataAccessException("That is not your piece!",0);
      }
   }

   private void leave(Session session, String message) throws IOException {
      session.getRemote().sendString("RESPONSE!: you have left game");
   }

   private void resign(Session session, String message) throws IOException {
      session.getRemote().sendString("RESPONSE!: you lost womp womp");
   }
}

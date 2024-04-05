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
            ErrorMessage error = new ErrorMessage("Error: " + e.getMessage());
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

   private void broadcast(List<Session> sessions, String message, Session exclude) throws IOException {
      Notification notification = new Notification(message);
      String json = serializer.toJson(notification);
      for (Session session : sessions) {
         if (session != exclude) {
            session.getRemote().sendString(json);
         }
      }
   }

   private void join(Session session, String json) throws IOException, DataAccessException {
      JoinPlayer command = serializer.fromJson(json, JoinPlayer.class);
      gameDAO = new SQLGameDAO();
      GameData gameData = gameDAO.getGame(command.getGameID());
      authDAO = new SQLAuthDAO();
      String username = authDAO.getAuth(command.getAuthString()).username();
      if (command.getPlayerColor() == ChessGame.TeamColor.WHITE && !Objects.equals(gameData.whiteUsername(), username)
              || command.getPlayerColor() == ChessGame.TeamColor.BLACK && !Objects.equals(gameData.blackUsername(), username)) {
         throw new DataAccessException("Illegal join", 0);
      }
      sessions.put(session, command.getColorString());
      ChessGame game = gameData.game();
      addToGroup(command.getGameID(), session);
      LoadGame response = new LoadGame(game, command.getPlayerColor());
      String responseJson = serializer.toJson(response);
      session.getRemote().sendString(responseJson);
      broadcast(gameGroups.get(command.getGameID()), username + " has joined the game as " + command.getColorString(), session);
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
      broadcast(gameGroups.get(command.getGameID()), username + " is now watching this game.", session );
   }

   private void move(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {
      MakeMove command = serializer.fromJson(message, MakeMove.class);
      GameData gameData = gameDAO.getGame(command.getGameID());
      ChessGame game = gameData.game();
      if (game.isGameOver()) {
         throw new InvalidMoveException("The game is over, you can no longer make moves. Use command \"leave\" to exit.");
      }
      authChecker(session, game.getBoard().getPiece(command.getMove().getStartPosition()));
      game.makeMove(command.getMove());
      String boardData = serializer.toJson(game);
      gameDAO.updateBoard(boardData, command.getGameID());

      for (Session allSessions : gameGroups.get(command.getGameID())) {
         ChessGame.TeamColor color = (Objects.equals(sessions.get(allSessions), "black")) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
         LoadGame response = new LoadGame(game, color);
         String json = serializer.toJson(response);
         allSessions.getRemote().sendString(json);
      }
      String username = authDAO.getAuth(command.getAuthString()).username();
      broadcast(gameGroups.get(command.getGameID()), username + " has moved " + command.getMoveStr(), session);
      checkCheck(gameData, command.getGameID());
   }

   private void checkCheck(GameData gameData, int gameID) throws IOException, DataAccessException {
      ChessGame game = gameData.game();
      String whiteUsername = gameData.whiteUsername();
      String blackUsername = gameData.blackUsername();
      if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
         broadcast(gameGroups.get(gameData.gameID()),"!! " + blackUsername + " IS IN CHECKMATE !!\nThe game is over, " + whiteUsername + " has won! Use command \"leave\" to exit.", null);
         endGame(game, gameID);
      }
      else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
         broadcast(gameGroups.get(gameData.gameID()),"!! " + whiteUsername + " IS IN CHECKMATE !!\nThe game is over, " + blackUsername + " has won! Use command \"leave\" to exit.", null);
         endGame(game, gameID);
      }
      if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
         broadcast(gameGroups.get(gameData.gameID()),"!! " + blackUsername + " IS IN STALEMATE !!\nThe game is over, " + whiteUsername + " has won! Use command \"leave\" to exit.", null);
         endGame(game, gameID);
      }
      else if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
         broadcast(gameGroups.get(gameData.gameID()),"!! " + whiteUsername + " IS IN STALEMATE !!\nThe game is over, " + blackUsername + " has won! Use command \"leave\" to exit.", null);
         endGame(game, gameID);
      }
      else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
         broadcast(gameGroups.get(gameData.gameID()),"!! " + blackUsername + " IS IN CHECK !!", null);
      }
      else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
         broadcast(gameGroups.get(gameData.gameID()),"!! " + whiteUsername + " IS IN CHECK !!", null);
      }
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

   private void leave(Session session, String message) throws IOException, DataAccessException {
      Leave command = serializer.fromJson(message, Leave.class);
      GameData game = gameDAO.getGame(command.getGameID());
      String username = authDAO.getAuth(command.getAuthString()).username();
      if (Objects.equals(sessions.get(session), "white")) {
         gameDAO.leaveGame(ChessGame.TeamColor.WHITE, game);
         broadcast(gameGroups.get(command.getGameID()), username + " has left the game.", session);
      }
      else if (Objects.equals(sessions.get(session), "black")) {
         gameDAO.leaveGame(ChessGame.TeamColor.BLACK, game);
         broadcast(gameGroups.get(command.getGameID()), username + " has left the game.", session);
      }
      else {
         broadcast(gameGroups.get(command.getGameID()), username + " has stopped watching", session);
      }
      gameGroups.get(command.getGameID()).remove(session);
   }

   private void resign(Session session, String message) throws Exception {
      Resign command = serializer.fromJson(message, Resign.class);
      if (Objects.equals(sessions.get(session), "observer")) {
         throw new Exception("You are not playing this game.");
      }
      String username = authDAO.getAuth(command.getAuthString()).username();
      List<Session> group = gameGroups.get(command.getGameID());
      for (Session sesh : group) {
         if (Objects.equals(sessions.get(sesh), "resign")) {
            throw new Exception("You can't resign; the other player already did. Use \"leave\" instead.");
         }
      }
      sessions.put(session, "resign");
      broadcast(gameGroups.get(command.getGameID()), username + " has resigned.\nThe game is over. Use command \"leave\" to exit.", null);
      ChessGame game = gameDAO.getGame(command.getGameID()).game();
      endGame(game, command.getGameID());
   }

   private void endGame(ChessGame game, int gameID) throws DataAccessException {
      game.gameOver();
      String json = serializer.toJson(game);
      gameDAO.updateBoard(json, gameID);
   }
}

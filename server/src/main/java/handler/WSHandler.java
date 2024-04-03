package handler;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.userCommands.*;

import java.io.IOException;

@WebSocket
public class WSHandler {
   Gson serializer = new Gson();
   @OnWebSocketMessage
   public void onMessage(Session session, String message) throws Exception {
      UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
      switch (command.getCommandType()) {
         case JOIN_PLAYER -> join(session, (JoinPlayer) command);
         case JOIN_OBSERVER -> observe(session, (JoinObserver) command);
         case MAKE_MOVE -> move(session, (MakeMove) command);
         case LEAVE -> leave(session, (Leave) command);
         case RESIGN -> resign(session, (Resign) command);
      }
   }

   private void join(Session session, JoinPlayer command) throws IOException {
      LoadGame response = new LoadGame(command.getGameID());
      String json = serializer.toJson(response);
      session.getRemote().sendString(json);
   }

   private void observe(Session session, JoinObserver command) throws IOException {
      LoadGame response = new LoadGame(command.getGameID());
      String json = serializer.toJson(response);
      session.getRemote().sendString(json);
   }

   private void move(Session session, MakeMove command) throws IOException {
      session.getRemote().sendString("RESPONSE!: imagine that piece just moved.");
   }

   private void leave(Session session, Leave command) throws IOException {
      session.getRemote().sendString("RESPONSE!: you have left game");
   }

   private void resign(Session session, Resign command) throws IOException {
      session.getRemote().sendString("RESPONSE!: you lost womp womp");
   }
}

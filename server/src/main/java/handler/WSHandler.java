package handler;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WSHandler {
   @OnWebSocketMessage
   public void onMessage(Session session, String message) throws Exception {
      UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

      //var conn = getConnection(command.authToken, session);
      //if (conn != null) {
         switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(session);
            case JOIN_OBSERVER -> observe(session);
            case MAKE_MOVE -> move(session);
            case LEAVE -> leave(session);
            case RESIGN -> resign(session);
         }
      //} else {
         //Connection.sendError(session.getRemote(), "unknown user");
      //}
   }

   private void join(Session session) throws IOException {
      session.getRemote().sendString("RESPONSE!: you have joined game.");
   }

   private void observe(Session session) throws IOException {
      session.getRemote().sendString("RESPONSE!: you are watching game.");
   }

   private void move(Session session) throws IOException {
      session.getRemote().sendString("RESPONSE!: imagine that piece just moved.");
   }

   private void leave(Session session) throws IOException {
      session.getRemote().sendString("RESPONSE!: you have left game");
   }

   private void resign(Session session) throws IOException {
      session.getRemote().sendString("RESPONSE!: you lost womp womp");
   }
}

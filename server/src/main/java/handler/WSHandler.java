package handler;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WSHandler {
   @OnWebSocketMessage
   public void onMessage(Session session, String message) throws Exception {
      session.getRemote().sendString("RESPONSE!: " + message);
   }
}

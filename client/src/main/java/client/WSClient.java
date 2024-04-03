package client;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.*;

import javax.websocket.*;
import java.net.URI;

import static ui.EscapeSequences.*;

public class WSClient extends Endpoint {
   private Session session;
   public WSClient() throws Exception {
      URI uri = new URI("ws://localhost:8080/connect");
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, uri);

      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
         @Override
         public void onMessage(String message) {
            try {
               ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
               WSClient.this.notify(serverMessage);
            } catch(Exception ex) {
               WSClient.this.notify(new ErrorMessage(ex.getMessage()));
            }
         }
      });
   }


   public void notify(ServerMessage message) {
      switch (message.getServerMessageType()) {
         case NOTIFICATION -> displayNotification(((Notification) message).getMessage());
         case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
         case LOAD_GAME -> loadGame(((LoadGame) message).getGame());
      }
   }

   public void displayNotification(String message) {
      System.out.println(SET_TEXT_COLOR_GREEN + message);
   }

   public void displayError(String message) {
      System.out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "⚠ " + message + " ⚠" + RESET_TEXT_BOLD_FAINT);
   }

   public void loadGame(int game) {
      System.out.println(SET_TEXT_COLOR_WHITE + "uh yeah here's the game");
   }



   public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}

   @Override
   public void onOpen(Session session, EndpointConfig endpointConfig) {
   }
}

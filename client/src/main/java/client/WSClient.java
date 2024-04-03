package client;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.BoardPrinter;
import ui.Gameplay;
import webSocketMessages.serverMessages.*;

import javax.websocket.*;
import java.net.URI;

import static ui.EscapeSequences.*;

public class WSClient extends Endpoint {
   private Session session;
   private Gson serializer = new Gson();
   public WSClient() throws Exception {
      URI uri = new URI("ws://localhost:8080/connect");
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, uri);

      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
         @Override
         public void onMessage(String message) {
            try {
               WSClient.this.notify(message);
            } catch(Exception ex) {
               System.out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "⚠ " + ex.getMessage() + " ⚠" + RESET_TEXT_BOLD_FAINT);
            }
         }
      });
   }


   public void notify(String message) {
      ServerMessage serverMessage = serializer.fromJson(message, ServerMessage.class);
      switch (serverMessage.getServerMessageType()) {
         case NOTIFICATION -> displayNotification(message);
         case ERROR -> displayError(message);
         case LOAD_GAME -> loadGame(message);
      }
   }

   public void displayNotification(String message) {
      String notification = serializer.fromJson(message, Notification.class).getMessage();
      System.out.println(SET_TEXT_COLOR_GREEN + notification);
   }

   public void displayError(String message) {
      String error = serializer.fromJson(message, ErrorMessage.class).getErrorMessage();
      System.out.println(SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "⚠ " + error + " ⚠" + RESET_TEXT_BOLD_FAINT);
   }

   public void loadGame(String message) {
      ChessGame game = serializer.fromJson(message, LoadGame.class).getGame();
      BoardPrinter printer = new BoardPrinter();
      System.out.println(printer.printBoard(game.getBoard(), ChessGame.TeamColor.WHITE, null, null));
   }



   public void send(String msg) throws Exception {this.session.getBasicRemote().sendText(msg);}

   @Override
   public void onOpen(Session session, EndpointConfig endpointConfig) {
   }
}

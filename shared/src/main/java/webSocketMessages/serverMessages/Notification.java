package webSocketMessages.serverMessages;

public class Notification extends ServerMessage{
   private String message;

   public Notification(String message) {
      serverMessageType = ServerMessageType.NOTIFICATION;
      this.message = message;
   }

   public String getMessage() {return message;}
}

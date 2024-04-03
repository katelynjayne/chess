package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
   private String errorMessage;
   public ErrorMessage(String errorMessage) {
      serverMessageType = ServerMessageType.ERROR;
      this.errorMessage = errorMessage;
   }

   public String getErrorMessage() {return errorMessage;}
}

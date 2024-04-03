package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
   private int gameID;

   public Resign(String authToken, int gameID) {
      super(authToken);
      commandType = CommandType.RESIGN;
      this.gameID = gameID;
   }
}

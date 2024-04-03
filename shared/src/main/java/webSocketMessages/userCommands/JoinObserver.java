package webSocketMessages.userCommands;


public class JoinObserver extends UserGameCommand {

   private int gameID;

   public JoinObserver(String authToken, int gameID) {
      super(authToken);
      commandType = UserGameCommand.CommandType.JOIN_OBSERVER;
      this.gameID = gameID;
   }

   public int getGameID() {return gameID;}
}

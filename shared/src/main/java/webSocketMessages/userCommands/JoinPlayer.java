package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {

   private int gameID;
   private ChessGame.TeamColor color;

   public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor color) {
      super(authToken);
      commandType = CommandType.JOIN_PLAYER;
      this.gameID = gameID;
      this.color = color;
   }
}

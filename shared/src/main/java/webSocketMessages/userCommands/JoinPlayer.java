package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {

   private int gameID;
   private ChessGame.TeamColor playerColor;

   public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor color) {
      super(authToken);
      commandType = CommandType.JOIN_PLAYER;
      this.gameID = gameID;
      this.playerColor = color;
   }

   public int getGameID() {return gameID;}

   public String getColorString() {
      return (playerColor == ChessGame.TeamColor.WHITE) ? "white" : "black";
   }
}

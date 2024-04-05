package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
   private int gameID;
   private ChessMove move;
   String moveStr;

   public MakeMove(String authToken, int gameID, ChessMove move, String moveStr) {
      super(authToken);
      commandType = CommandType.MAKE_MOVE;
      this.gameID = gameID;
      this.move = move;
      this.moveStr = moveStr;
   }

   public int getGameID() {return gameID;}

   public ChessMove getMove() {return move;}

   public String getMoveStr() {return moveStr;}

}

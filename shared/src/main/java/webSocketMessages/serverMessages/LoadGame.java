package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {

   private ChessGame game;
   private ChessGame.TeamColor color;

   public LoadGame(ChessGame game, ChessGame.TeamColor color) {
      serverMessageType = ServerMessageType.LOAD_GAME;
      this.game = game;
      this.color = color;
   }

   public ChessGame getGame() {return game;}
   public ChessGame.TeamColor getColor() {return color;}
}

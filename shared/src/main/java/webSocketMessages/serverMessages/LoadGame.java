package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {

   private ChessGame game;

   public LoadGame(ChessGame game) {
      serverMessageType = ServerMessageType.LOAD_GAME;
      this.game = game;
   }

   public ChessGame getGame() {return game;}
}

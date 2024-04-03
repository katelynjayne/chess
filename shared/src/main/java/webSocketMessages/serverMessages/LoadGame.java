package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {

   private int game;

   public LoadGame(int game) {
      serverMessageType = ServerMessageType.LOAD_GAME;
      this.game = game;
   }

   public int getGame() {return game;}
}

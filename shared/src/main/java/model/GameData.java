package model;

import chess.ChessGame;

public class GameData {
   private int gameID;
   private String whiteUsername;
   private String blackUsername;
   private String gameName;
   private ChessGame game;
   public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
      this.gameID = gameID;
      this.whiteUsername = whiteUsername;
      this.blackUsername = blackUsername;
      this.gameName = gameName;
      this.game = game;
   }
   public int getGameID() {
      return gameID;
   }
   public boolean setUsername(ChessGame.TeamColor color, String username) {
      if (color == ChessGame.TeamColor.WHITE && whiteUsername == null) {
         whiteUsername = username;
         return true;
      }
      else if (blackUsername == null){
         blackUsername = username;
         return true;
      }
      return false;

   }
}



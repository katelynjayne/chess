package model;

import chess.ChessGame;

import java.util.Objects;

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
   public String getWhiteUsername() {
      return whiteUsername;
   }
   public boolean setUsername(ChessGame.TeamColor color, String username) {
      if (color == ChessGame.TeamColor.WHITE && whiteUsername == null) {
         whiteUsername = username;
         return true;
      }
      else if (color == ChessGame.TeamColor.BLACK && blackUsername == null){
         blackUsername = username;
         return true;
      }
      return false;

   }

   @Override
   public String toString() {
      return "GameData{" +
              "gameID=" + gameID +
              ", whiteUsername='" + whiteUsername + '\'' +
              ", blackUsername='" + blackUsername + '\'' +
              ", gameName='" + gameName + '\'' +
              ", game=" + game +
              '}';
   }

   @Override
   public int hashCode() {
      return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      GameData other = (GameData) o;
      return (other.gameID == gameID)
              && (Objects.equals(other.whiteUsername, whiteUsername))
              && (Objects.equals(other.blackUsername, blackUsername))
              && (Objects.equals(other.gameName, gameName))
              && (Objects.equals(other.game, game));
   }
}



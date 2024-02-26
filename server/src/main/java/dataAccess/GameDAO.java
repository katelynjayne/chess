package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
   void clear();
   Collection<GameData> listGames();
   int createGame(String gameName);
   GameData getGame(int gameID);
   boolean updateGame(ChessGame.TeamColor color, String username, GameData game);
}

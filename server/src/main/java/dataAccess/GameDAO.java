package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
   void clear() throws DataAccessException;
   Collection<GameData> listGames() throws DataAccessException;
   int createGame(String gameName) throws DataAccessException;
   GameData getGame(int gameID) throws DataAccessException;
   boolean updateGame(ChessGame.TeamColor color, String username, GameData game) throws DataAccessException;

   void updateBoard(String gameData, int gameID) throws DataAccessException;
}

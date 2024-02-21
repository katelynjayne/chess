package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;

public class CreateGameService extends Service {
   public int createGame(String authToken, GameData game) throws DataAccessException {
      checkAuth(authToken);
      return gameDAO.createGame(game);
   }
}

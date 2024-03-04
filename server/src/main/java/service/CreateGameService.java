package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.GameData;

public class CreateGameService extends Service {
   public CreateGameService() throws DataAccessException {
   }

   public int createGame(String authToken, String gameName) throws DataAccessException {
      checkAuth(authToken);
      return gameDAO.createGame(gameName);
   }
}

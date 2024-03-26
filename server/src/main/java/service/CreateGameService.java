package service;

import dataAccess.DataAccessException;

public class CreateGameService extends Service {
   public CreateGameService() throws DataAccessException {
   }

   public int createGame(String authToken, String gameName) throws DataAccessException {
      checkAuth(authToken);
      return gameDAO.createGame(gameName);
   }
}

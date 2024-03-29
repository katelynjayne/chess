package service;

import dataAccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public class ListGamesService extends Service {
   public ListGamesService() throws DataAccessException {
   }

   public Collection<GameData> listGames(String authToken) throws DataAccessException {
      checkAuth(authToken);
      return gameDAO.listGames();
   }
}

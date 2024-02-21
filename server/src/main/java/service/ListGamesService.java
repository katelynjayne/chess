package service;

import dataAccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public class ListGamesService extends Service {
   public Collection<GameData> listGames(String authToken) throws DataAccessException {
      if (authDAO.getAuth(authToken) == null) {
         throw new DataAccessException("Error: unauthorized");
      }
      return gameDAO.listGames();
   }
}

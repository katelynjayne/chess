package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;

public class JoinGameService extends Service{
   public JoinGameService() throws DataAccessException {
   }

   public void joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws DataAccessException {
      AuthData auth = checkAuth(authToken);
      GameData game = gameDAO.getGame(gameID);
      if (game == null) {
         throw new DataAccessException("Error: bad request",400);
      }
      if (playerColor != null) {
         if (!gameDAO.updateGame(playerColor, auth.username(), game)) {
            throw new DataAccessException("Error: already taken",403);
         }
      }
   }
}

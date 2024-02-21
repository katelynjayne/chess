package service;

import chess.ChessGame;
import dataAccess.DataAccessException;

public class JoinGameService extends Service{
   public void joinGame(String authToken, ChessGame game) throws DataAccessException {
      checkAuth(authToken);
   }
}

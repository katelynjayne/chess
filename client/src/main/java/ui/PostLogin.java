package ui;

import chess.ChessGame;
import client.ServerFacade;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;

public class PostLogin {
   private String authToken;
   private final ServerFacade facade = new ServerFacade(8080);
   private final HashMap<Integer, GameData> uiIDs = new HashMap<>();

   public PostLogin(String authToken) {
      this.authToken = authToken;
   }

   public void setAuth (String authToken) {
      this.authToken = authToken;
   }

   public String help() {
      return SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "create <new game name>" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to create your own game\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "list" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to list all current games\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "join <game ID> <WHITE/BLACK>" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to join a game\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "watch <game ID>" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to observe a game\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "help" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to print this list of commands\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "logout" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to logout";
   }

   public String create(String[] params) throws Exception {
      if (params.length == 0) {
         throw new IllegalArgumentException("Please specify game name.");
      }
      String gameName = String.join("-", params);
      facade.createGame(authToken, gameName);
      return gameName;
   }

   public String list() throws Exception {
      Collection<GameData> list = facade.listGames(authToken).games();
      StringBuilder output = new StringBuilder(SET_TEXT_COLOR_YELLOW);
      int counter = 1;
      if (list.isEmpty()) {
         output.append("No active games. Use the command \"create\" to start your own.");
      }
      for (GameData game : list) {
         if (counter != 1) {
            output.append("\n");
         }
         output.append(counter).append(": ").append(game.gameName());
         String whiteUser = (game.whiteUsername() != null) ? game.whiteUsername() : "FREE";
         String blackUser = (game.blackUsername() != null) ? game.blackUsername() : "FREE";
         output.append(" | WHITE: ").append(whiteUser).append(" | BLACK: ").append(blackUser);
         if (!uiIDs.containsKey(counter)) {
            uiIDs.put(counter, game);
         }
         counter += 1;
      }
      return output.toString();
   }

   public void join(String[] params, Gameplay gameplay) throws Exception {
      if (params.length != 2) {
         throw new IllegalArgumentException("Please specify game ID and color.");
      }
      int id = parseInt(params[0]);
      ChessGame.TeamColor color;
      if (Objects.equals(params[1], "white")) {
         color = ChessGame.TeamColor.WHITE;
      }
      else if (Objects.equals(params[1], "black")) {
         color = ChessGame.TeamColor.BLACK;
      }
      else {
         throw new IllegalArgumentException("Please specify \"white\" or \"black\" for color.");
      }
      int gameID;
      try {
         gameID = uiIDs.get(id).gameID();
      }
      catch (NullPointerException e){
         throw new Exception("Couldn't find game. Try the \"list\" command to see all active games and corresponding IDs.");
      }
      facade.joinGame(authToken, color, gameID);
      gameplay.setGame(uiIDs.get(id).game(), color, authToken, gameID);
      gameplay.join();
   }

   public void watch(String[] params, Gameplay gameplay) throws Exception {
      if (params.length != 1) {
         throw new IllegalArgumentException("Please specify game ID.");
      }
      int id = parseInt(params[0]);
      int gameID;
      try {
         gameID = uiIDs.get(id).gameID();
      }
      catch (NullPointerException e){
         throw new Exception("Couldn't find game. Try the \"list\" command to see all active games and corresponding IDs.");
      }
      facade.joinGame(authToken, null, gameID);
      gameplay.setGame(uiIDs.get(id).game(), ChessGame.TeamColor.WHITE, authToken, gameID);
      gameplay.watch();
   }

   public void logout() throws Exception {
      facade.logout(authToken);
   }
}

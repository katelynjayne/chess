import chess.ChessGame;
import client.ServerFacade;
import model.AuthData;
import model.GameData;
import ui.Gameplay;
import ui.PostLogin;
import ui.PreLogin;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;

public class Client {
   private final PreLogin preLogin = new PreLogin();
   private final PostLogin postLogin = new PostLogin(null);
   private final Gameplay gameplay = new Gameplay();
   private boolean loggedIn = false;
   private boolean inGame = false;
   private final ServerFacade facade = new ServerFacade(8080);
   private final HashMap<Integer, GameData> uiIDs = new HashMap<>();
   public String eval(String input) {
      try {
         var tokens = input.toLowerCase().split(" ");
         String cmd = (tokens.length > 0) ? tokens[0] : "help";
         var params = Arrays.copyOfRange(tokens, 1, tokens.length);
         return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "create" -> create(params);
            case "list" -> list();
            case "join" -> join(params);
            case "watch" -> watch(params);
            case "logout" -> logout();
            case "quit" -> "";
            default -> help();
         };
      }
      catch (Exception e) {
         return SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "⚠ " + e.getMessage() + " ⚠" + RESET_TEXT_BOLD_FAINT;
      }

   }

   private String register(String[] params) throws Exception{
      if (params.length != 3) {
         throw new IllegalArgumentException("Please specify username, password, and email.");
      }
      facade.register(params[0],params[1],params[2]);
      login(Arrays.copyOfRange(params, 0, 2));
      return SET_TEXT_COLOR_GREEN + "Successfully registered " + params[0] + ". You are now logged in.";
   }

   private String login(String[] params) throws Exception {
      if (params.length != 2) {
         throw new IllegalArgumentException("Please specify username and password.");
      }
      AuthData auth = facade.login(params[0], params[1]);
      postLogin.setAuth(auth.authToken());
      loggedIn = true;
      return SET_TEXT_COLOR_GREEN + "Successfully logged in.";
   }

   private String help() {
      if (inGame) {
         return gameplay.help();
      }
      if (loggedIn) {
         return postLogin.help();
      }
      return preLogin.help();
   }

   private String create(String[] params) throws Exception{
      if (params.length == 0) {
         throw new IllegalArgumentException("Please specify game name.");
      }
      String gameName = String.join("-", params);
      facade.createGame(postLogin.getAuthToken(), gameName);
      return SET_TEXT_COLOR_GREEN + "Successfully created game: " + gameName;
   }

   private String list() throws Exception {
      Collection<GameData> list = facade.listGames(postLogin.getAuthToken()).getGames();
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
         String whiteUser = (game.whiteUsername() != null)?game.whiteUsername():"FREE";
         String blackUser = (game.blackUsername() != null)?game.blackUsername():"FREE";
         output.append(" | WHITE: ").append(whiteUser).append(" | BLACK: ").append(blackUser);
         if (!uiIDs.containsKey(counter)) {
            uiIDs.put(counter, game);
         }
         counter += 1;
      }
      return output.toString();
   }

   private String join(String[] params) throws Exception {
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
      Gameplay gameplay = new Gameplay();
      int gameID;
      try {
         gameID = uiIDs.get(id).gameID();
      }
      catch (NullPointerException e){
         throw new Exception("Couldn't find game. Try the \"list\" command to see all active games and corresponding IDs.");
      }
      facade.joinGame(postLogin.getAuthToken(), color, gameID);
      inGame = true;
      return SET_TEXT_COLOR_GREEN + "Successfully joined game " + params[0] + " as " + params[1] + ". Enjoy your game!\n"
              + gameplay.makeBoard(uiIDs.get(id).game().getBoard(), color);
   }

   private String watch(String[] params) throws Exception {
      if (params.length != 1) {
         throw new IllegalArgumentException("Please specify game ID.");
      }
      int id = parseInt(params[0]);
      Gameplay gameplay = new Gameplay();
      int gameID;
      try {
         gameID = uiIDs.get(id).gameID();
      }
      catch (NullPointerException e){
         throw new Exception("Couldn't find game. Try the \"list\" command to see all active games and corresponding IDs.");
      }
      facade.joinGame(postLogin.getAuthToken(), null, gameID);
      inGame = true;
      return gameplay.makeBoard(uiIDs.get(id).game().getBoard(), ChessGame.TeamColor.WHITE);
   }

   private String logout() throws Exception {
      facade.logout(postLogin.getAuthToken());
      loggedIn = false;
      return SET_TEXT_COLOR_GREEN + "Successfully logged out.";
   }
}

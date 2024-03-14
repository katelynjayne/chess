import model.AuthData;
import ui.Gameplay;
import ui.PostLogin;
import ui.PreLogin;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class Client {
   PreLogin preLogin = new PreLogin();
   PostLogin postLogin = new PostLogin(null);
   boolean loggedIn = false;
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
      ServerFacade facade = new ServerFacade(8080);
      facade.register(params[0],params[1],params[2]);
      login(Arrays.copyOfRange(params, 0, 2));
      return SET_TEXT_COLOR_GREEN + "Successfully registered " + params[0] + ". You are now logged in.";
   }

   private String login(String[] params) throws Exception {
      if (params.length != 2) {
         throw new IllegalArgumentException("Please specify username and password.");
      }
      ServerFacade facade = new ServerFacade(8080);
      AuthData auth = facade.login(params[0], params[1]);
      postLogin.setAuth(auth.authToken());
      loggedIn = true;
      return SET_TEXT_COLOR_GREEN + "Successfully logged in.";
   }

   private String help() {
      if (loggedIn) {
         return postLogin.help();
      }
      return preLogin.help();
   }

   private String create(String[] params) throws IllegalArgumentException{
      if (params.length == 0) {
         throw new IllegalArgumentException("Please specify game name.");
      }
      String gameName = String.join("-", params);
      return SET_TEXT_COLOR_GREEN + "Successfully created game: " + gameName;
   }

   private String list() {
      return "Imagine this is a list of games";
   }

   private String join(String[] params) throws IllegalArgumentException {
      if (params.length != 2) {
         throw new IllegalArgumentException("Please specify game ID and color.");
      }
      return SET_TEXT_COLOR_GREEN + "Successfully joined game " + params[0] + " as color " + params[1] + ". Enjoy your game!";
      //also print out game
   }

   private String watch(String[] params) throws IllegalArgumentException {
      if (params.length != 1) {
         throw new IllegalArgumentException("Please specify game ID.");
      }
      Gameplay gameplay = new Gameplay();
      return gameplay.makeBoard();
   }

   private String logout() {
      loggedIn = false;
      return SET_TEXT_COLOR_GREEN + "Successfully logged out.";
   }
}

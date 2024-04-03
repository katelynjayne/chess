import chess.ChessGame;
import client.ServerFacade;
import client.WSClient;
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
   public String eval(String input) {
      try {
         var tokens = input.toLowerCase().split(" ");
         String cmd = (tokens.length > 0) ? tokens[0] : "help";
         var params = Arrays.copyOfRange(tokens, 1, tokens.length);
         if (inGame) {
            return switch (cmd) {
               case "redraw" -> redraw();
               case "move" -> move(params);
               case "highlight" -> highlight(params);
               case "leave" -> leave();
               case "resign" -> resign();
               default -> help();
            };
         }
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
      preLogin.register(params);
      login(Arrays.copyOfRange(params, 0, 2));
      return SET_TEXT_COLOR_GREEN + "Successfully registered " + params[0] + ". You are now logged in.";
   }

   private String login(String[] params) throws Exception {
      postLogin.setAuth(preLogin.login(params));
      loggedIn = true;
      return SET_TEXT_COLOR_GREEN + "Successfully logged in.";
   }

   private String help() {
      if (inGame) {
         return gameplay.help();
      }
      else if (loggedIn) {
         return postLogin.help();
      }
      return preLogin.help();
   }

   private String create(String[] params) throws Exception{
      String gameName = postLogin.create(params);
      return SET_TEXT_COLOR_GREEN + "Successfully created game: " + gameName;
   }

   private String list() throws Exception {
      return postLogin.list();
   }

   private String join(String[] params) throws Exception {
      postLogin.join(params, gameplay);
      inGame = true;
      return gameplay.makeBoard();
   }

   private String watch(String[] params) throws Exception {
      postLogin.watch(params, gameplay);
      inGame = true;
      return gameplay.makeBoard();
   }

   private String logout() throws Exception {
      postLogin.logout();
      loggedIn = false;
      return SET_TEXT_COLOR_GREEN + "Successfully logged out.";
   }

   private String redraw() {
      return gameplay.makeBoard();
   }

   private String move(String[] params) throws Exception {
      gameplay.move(params);
      return gameplay.makeBoard();
   }

   private String highlight(String[] params) throws Exception{
      return gameplay.highlight(params);
   }

   private String leave() throws Exception {
      gameplay.leave();
      inGame = false;
      return "";

   }

   private String resign() throws Exception {
      gameplay.resign();
      inGame = false;
      return "";
   }
}

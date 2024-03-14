package ui;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PostLogin {
   private String authToken;

   public PostLogin(String authToken) {
      this.authToken = authToken;
   }

   public void setAuth (String authToken) {
      this.authToken = authToken;
   }

   public String getAuthToken() {
      return authToken;
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
}

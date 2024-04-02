package ui;

import client.ServerFacade;
import model.AuthData;

import static ui.EscapeSequences.*;


public class PreLogin {
   private final ServerFacade facade = new ServerFacade(8080);
   public String help() {
      return SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "register <username> <password> <email>" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to create a new account\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "login <username> <password>" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to login to an existing account\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "help" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to print this list of commands\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "quit" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to exit";
   }

   public void register(String[] params) throws Exception{
      if (params.length != 3) {
         throw new IllegalArgumentException("Please specify username, password, and email.");
      }
      facade.register(params[0],params[1],params[2]);
   }

   public String login(String[] params) throws Exception {
      if (params.length != 2) {
         throw new IllegalArgumentException("Please specify username and password.");
      }
      AuthData auth = facade.login(params[0], params[1]);
      return auth.authToken();
   }
}

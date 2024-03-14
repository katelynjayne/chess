package ui;

import java.util.Arrays;

import static ui.EscapeSequences.*;


public class PreLogin {
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
}

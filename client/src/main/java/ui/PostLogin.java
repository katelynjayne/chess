package ui;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PostLogin {
   public String help() {
      return SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "create <new game name>" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " creates your own game\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "list" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " lists all current games\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "quit" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to exit";
   }
}

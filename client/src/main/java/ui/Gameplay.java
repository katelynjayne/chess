package ui;
import chess.ChessBoard;

import static ui.EscapeSequences.*;
public class Gameplay {
   public String makeBoard() {
      ChessBoard board = new ChessBoard();
      board.resetBoard();
      StringBuilder output = new StringBuilder(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_WHITE);
      output.append("    a  b  c  d  e  f  g  h    " + REAL_RESET + "\n");
      boolean black = true;
      for (int i = 0; i < 8; i++) {
         output.append(SET_BG_COLOR_DARK_GREEN + " ").append(i + 1).append(" ");
         black = !black;
         for (int j = 0; j < 8; j ++) {
            if (black) {
               output.append(SET_BG_COLOR_LIGHT_GREY);
               black = false;
            }
            else {
               output.append(SET_BG_COLOR_WHITE);
               black = true;
            }
            if (board.getPiece(i,j) == null) {
               output.append(EMPTY);
            }
            else {
               output.append(SET_TEXT_COLOR_BLACK+ board.getPiece(i,j).toString()+ SET_TEXT_COLOR_WHITE);
            }
         }
         output.append(SET_BG_COLOR_DARK_GREEN + " ").append(i + 1).append(" "+ REAL_RESET + "\n");
      }
      output.append(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_WHITE + "    a  b  c  d  e  f  g  h    " + REAL_RESET);
      return output.toString();
   }
}

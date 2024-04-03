package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.List;
import java.util.Map;

import static ui.EscapeSequences.*;

public class BoardPrinter {
   public String printBoard(ChessBoard board, ChessGame.TeamColor color, ChessPosition highlight, Map<Integer, List<Integer>> validMoves) {
      StringBuilder output = new StringBuilder(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_WHITE);
      String header=(color == ChessGame.TeamColor.BLACK)?"    h  g  f  e  d  c  b  a    " : "    a  b  c  d  e  f  g  h    ";
      output.append(header).append(REAL_RESET).append("\n");
      boolean blackSquare = true;
      int start=(color == ChessGame.TeamColor.WHITE)?7:0;
      int iterator=(color == ChessGame.TeamColor.WHITE)?-1:1;
      int end=(color == ChessGame.TeamColor.WHITE)?-1:8;
      for (int i = start; i != end; i+=iterator) {
         output.append(SET_BG_COLOR_DARK_GREEN + " ").append(i + 1).append(" ");
         blackSquare = !blackSquare;

         int innerStart=(color == ChessGame.TeamColor.WHITE)?0:7;
         int innerIterator=(color == ChessGame.TeamColor.WHITE)?1:-1;
         int innerEnd=(color == ChessGame.TeamColor.WHITE)?8:-1;
         for (int j = innerStart; j != innerEnd; j+=innerIterator) {
            if (blackSquare) {
               output.append(SET_BG_COLOR_LIGHT_GREY);
               blackSquare = false;
            }
            else {
               output.append(SET_BG_COLOR_WHITE);
               blackSquare = true;
            }
            if (highlight != null) {
               if (highlight.getRowIndex() == i && highlight.getColIndex() == j) {
                  output.append(SET_BG_COLOR_YELLOW);
               }
               else if (validMoves.containsKey(i)) {
                  if (validMoves.get(i).contains(j)) {
                     output.append(SET_BG_COLOR_GREEN);
                  }
               }
            }
            if (board.getPiece(i,j) == null) {
               output.append(EMPTY);
            }
            else {
               output.append(SET_TEXT_COLOR_BLACK).append(board.getPiece(i, j).toString()).append(SET_TEXT_COLOR_WHITE);
            }
         }
         output.append(SET_BG_COLOR_DARK_GREEN + " ").append(i + 1).append(" "+ REAL_RESET + "\n");
      }
      output.append(SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_WHITE).append(header).append(REAL_RESET);
      return output.toString();
   }
}

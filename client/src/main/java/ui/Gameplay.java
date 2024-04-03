package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.WSClient;
import com.google.gson.Gson;
import webSocketMessages.userCommands.JoinPlayer;

import java.util.*;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;
public class Gameplay {

   private ChessGame game;
   private ChessBoard board;
   private ChessGame.TeamColor color;

   private WSClient ws;

   public void setGame(ChessGame game, ChessGame.TeamColor color) {
      this.game = game;
      this.board = game.getBoard();
      this.color = color;
   }

   public String help() {
      return SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "redraw" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to see the board again\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "move <start position> <end position>" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to move a piece\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "highlight <position>" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to see all the moves a piece at the given position can make\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "leave" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to exit the game\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "resign" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to take the L\n" +
              SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "help" + RESET_TEXT_BOLD_FAINT
              + SET_TEXT_COLOR_BLUE + " to print this list of commands\n" +
              SET_TEXT_COLOR_YELLOW + "TIP: Please refer to positions in the form of <column letter><row number>. Example: a1" ;
   }
   public String makeBoard(ChessPosition highlight, Map<Integer, List<Integer>> validMoves) {
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

   public String makeBoard() {
      return makeBoard(null, null);
   }

   public void join(String authToken, int gameID) throws Exception {
      ws = new WSClient();
      JoinPlayer message = new JoinPlayer(authToken, gameID, color);
      String json = new Gson().toJson(message);
      ws.send(json);
   }

   public void watch() throws Exception {
      ws = new WSClient();
      ws.send("watch");
   }
   public String move(String[] params) throws Exception {
      if (params.length != 2) {
         throw new Exception("Please include the start position of the piece you will be moving, followed by the end position, separated by a space.");
      }
      return "";
   }

   public String highlight(String[] params) throws Exception{
      if (params.length != 1) {
         throw new Exception("Please specify the position you would like to highlight");
      }
      ChessPosition position = positionConverter(params[0]);
      Collection<ChessMove> validMoves = game.validMoves(position);
      Map<Integer, List<Integer>> coordinates = new HashMap<>();
      for (ChessMove move : validMoves) {
         int x = move.getEndPosition().getRowIndex();
         int y = move.getEndPosition().getColIndex();
         if (!coordinates.containsKey(x)) {
            coordinates.put(x, new ArrayList<>());
         }
         coordinates.get(x).add(y);
      }
      return makeBoard(position, coordinates);
   }

   private ChessPosition positionConverter(String position) throws IllegalArgumentException {
      if (position.length() != 2) {
         throw new IllegalArgumentException("Invalid position. TIP: Please refer to positions in the form of <column letter><row number>. Example: a1");
      }
      int colIndex = 0;
      int rowIndex = 0;
      boolean valid = true;
      if (position.charAt(0) >= 'a' && position.charAt(0) <= 'h') {
         colIndex = position.charAt(0) - 'a' + 1;
      }
      else {valid = false;}
      try {
         rowIndex = parseInt(String.valueOf(position.charAt(1)));
         if (rowIndex <= 0 || rowIndex > 8) {valid = false;}
      }
      catch (NumberFormatException e) {valid = false;}
      if (!valid) {
         throw new IllegalArgumentException("Invalid position. TIP: Please refer to positions in the form of <column letter><row number>. Example: a1");
      }
      return new ChessPosition(rowIndex , colIndex);
   }

   public String leave() throws Exception {
      ws.send("leave");
      return "";
   }

   public String resign() throws Exception {
      ws.send("resign");
      return "";
   }
}

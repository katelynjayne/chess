package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.WSClient;
import com.google.gson.Gson;
import webSocketMessages.userCommands.*;

import java.util.*;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;
public class Gameplay {

   private ChessGame game;
   private ChessBoard board;
   private ChessGame.TeamColor color;

   private WSClient ws;

   private String authToken;

   private int gameID;

   public void setGame(ChessGame game, ChessGame.TeamColor color, String authToken, int gameID) {
      this.game = game;
      this.board = game.getBoard();
      this.color = color;
      this.authToken = authToken;
      this.gameID = gameID;
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
      BoardPrinter printer = new BoardPrinter();
      return printer.printBoard(board,color,highlight,validMoves);
   }

   public String makeBoard() {
      return makeBoard(null, null);
   }

   public void join() throws Exception {
      ws = new WSClient();
      JoinPlayer message = new JoinPlayer(authToken, gameID, color);
      String json = new Gson().toJson(message);
      ws.send(json);
   }

   public void watch() throws Exception {
      ws = new WSClient();
      JoinObserver message = new JoinObserver(authToken, gameID);
      String json = new Gson().toJson(message);
      ws.send(json);
   }
   public void move(String[] params) throws Exception {
      if (params.length != 2) {
         throw new Exception("Please include the start position of the piece you will be moving, followed by the end position, separated by a space.");
      }
      ChessPosition startPosition = positionConverter(params[0]);
      ChessPosition endPosition = positionConverter(params[1]);
      MakeMove message = new MakeMove(authToken,gameID, new ChessMove(startPosition, endPosition));
      String json = new Gson().toJson(message);
      ws.send(json);
   }

   public String highlight(String[] params) throws Exception{
      if (params.length != 1) {
         throw new Exception("Please specify the position you would like to highlight");
      }
      ChessPosition position = positionConverter(params[0]);
      if (board.getPiece(position) == null) {
         throw new Exception("There is no piece at position " + params[0]);
      }
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
      Leave message = new Leave(authToken, gameID);
      String json = new Gson().toJson(message);
      ws.send(json);
      return SET_TEXT_COLOR_GREEN + "Successfully left game.";
   }

   public void resign() throws Exception {
      Resign message = new Resign(authToken, gameID);
      String json = new Gson().toJson(message);
      ws.send(json);
   }
}

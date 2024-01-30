package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        for (var move: potentialMoves) {
            ChessBoard boardCopy = board.copy();
            boardCopy.movePiece(move);
            if (!boardCopy.isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        if (validMoves(startPosition).contains(move)
                && board.getPiece(startPosition).getTeamColor() == teamTurn) {
            board.movePiece(move);
            if (teamTurn == TeamColor.BLACK) {
                teamTurn = TeamColor.WHITE;
            }
            else {
                teamTurn = TeamColor.BLACK;
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return board.isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean validMovesEmpty = true;
        for (int i = 1; i <= 8; i++) {
            for (int j=1; j <= 8; j++) {
                ChessPosition square=new ChessPosition(i, j);
                ChessPiece piece=board.getPiece(square);
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        if (!validMoves(square).isEmpty()) {
                            validMovesEmpty = false;
                        }
                    }
                }
            }
        }
        return isInCheck(teamColor) && validMovesEmpty;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean validMovesEmpty = true;
        for (int i = 1; i <= 8; i++) {
            for (int j=1; j <= 8; j++) {
                ChessPosition square=new ChessPosition(i, j);
                ChessPiece piece=board.getPiece(square);
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        if (!validMoves(square).isEmpty()) {
                            validMovesEmpty = false;
                        }
                    }
                }
            }
        }
        return !isInCheck(teamColor) && validMovesEmpty;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}

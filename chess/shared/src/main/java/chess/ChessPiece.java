package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessPosition position; // axel said i might need this...?
    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
        //this.position = position;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    public void setPosition(ChessPosition position) {
        this.position = position;
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        int rowIndex = myPosition.getRow() - 1;
        int colIndex = myPosition.getColumn() - 1;
        Collection<ChessMove> validMoves = new ArrayList<>();
        if (piece.type == PieceType.BISHOP) {
            // ok so we are going to look at all the spaces diagonal that are within the bounds of the board
            // and then check for pieces along each path.
            //look aat ChessMove class?
            //figure out Collection data structure
        }
        else if (piece.type == PieceType.KING) {
            // can move any direction ***i think*** but only one space
        }
        else if (piece.type == PieceType.KNIGHT) {
            if (rowIndex - 2 >= 0) {
                if (colIndex - 1 >= 0) {
                    ChessPosition newPosition = new ChessPosition(rowIndex - 3, colIndex - 2);
                    ChessMove validMove = new ChessMove(myPosition, newPosition, null);
                    validMoves.add(validMove);
                }
            }
            // ONLY HORSES CAN JUMP
            // 2 vert 1 horizontal + vice versa all directions
            // 8 possiblites
        }
        else if (piece.type == PieceType.PAWN) {
            // can move forward one
            // 2 on the first move?? is this included??
        }
        else if (piece.type == PieceType.QUEEN) {
            // can move any direction i believe***
        }
        else if (piece.type == PieceType.ROOK) {
            // can move in a straight line up or sideways as far as there are no pieces there
            //wait what about capturing other pieces????
            //hmmmmmm
        }
        return validMoves;
    }
}

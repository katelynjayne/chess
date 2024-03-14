package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class  ChessPiece {
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece=(ChessPiece) o;
        return color == piece.color && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        if (color == ChessGame.TeamColor.WHITE) {
            return switch (type) {
                case KING -> " ♔ ";
                case QUEEN -> " ♕ ";
                case BISHOP -> " ♗ ";
                case KNIGHT -> " ♘ ";
                case ROOK -> " ♖ ";
                case PAWN -> " ♙ ";
            };
        }
        else {
            return switch (type) {
                case KING -> " ♚ ";
                case QUEEN -> " ♛ ";
                case BISHOP -> " ♝ ";
                case KNIGHT -> " ♞ ";
                case ROOK -> " ♜ ";
                case PAWN -> " ♟ ";
            };
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessMoveCalculator calculator = new ChessMoveCalculator(this, board, myPosition);
        return switch(type) {
            case BISHOP -> calculator.bishopMoves();
            case KING -> calculator.kingMoves();
            case KNIGHT -> calculator.knightMoves();
            case PAWN -> calculator.pawnMoves();
            case QUEEN -> calculator.queenMoves();
            case ROOK -> calculator.rookMoves();
        };
    }
}

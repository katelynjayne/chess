package chess;

import javax.lang.model.type.ArrayType;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessPosition position; // axel said i might need this...?
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;


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

    private Boolean isInBounds(int rowIndex, int colIndex) {
       return rowIndex >= 0 && rowIndex < 8 && colIndex < 8 && colIndex >= 0;
    }
    private Boolean sameColorPiece(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) == null) {
            return false;
        }
        return board.getPiece(position).getTeamColor() == color;
    }

    private ChessMove validateMove(ChessBoard board, int rowIndex, int colIndex, ChessPosition currentPosition) {
        ChessPosition newPosition = new ChessPosition(rowIndex + 1, colIndex + 1);
        if (!sameColorPiece(board, newPosition)) {
            return new ChessMove(currentPosition, newPosition, null);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece=(ChessPiece) o;
        return Objects.equals(position, piece.position) && color == piece.color && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, color, type);
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
            // up and left
            int newRowIndex = rowIndex + 1;
            int newColIndex = colIndex - 1;
            while (isInBounds(newRowIndex, newColIndex)) {
                ChessMove validMove = validateMove(board, newRowIndex, newColIndex, myPosition);
                if (validMove == null) {
                    break;
                }
                validMoves.add(validMove);
                if (board.getPiece(validMove.getEndPosition()) != null) {
                    break;
                }
                newRowIndex += 1;
                newColIndex -= 1;
            }
            // up and right
            newRowIndex = rowIndex + 1;
            newColIndex = colIndex + 1;
            while (isInBounds(newRowIndex, newColIndex)) {
                ChessMove validMove = validateMove(board, newRowIndex, newColIndex, myPosition);
                if (validMove == null) {
                    break;
                }
                validMoves.add(validMove);
                if (board.getPiece(validMove.getEndPosition()) != null) {
                    break;
                }
                newRowIndex += 1;
                newColIndex += 1;
            }
            // down and left
            newRowIndex = rowIndex - 1;
            newColIndex = colIndex - 1;
            while (isInBounds(newRowIndex, newColIndex)) {
                ChessMove validMove = validateMove(board, newRowIndex, newColIndex, myPosition);
                if (validMove == null) {
                    break;
                }
                validMoves.add(validMove);
                if (board.getPiece(validMove.getEndPosition()) != null) {
                    break;
                }
                newRowIndex -= 1;
                newColIndex -= 1;
            }
            // down and right
            newRowIndex = rowIndex - 1;
            newColIndex = colIndex + 1;
            while (isInBounds(newRowIndex, newColIndex)) {
                ChessMove validMove = validateMove(board, newRowIndex, newColIndex, myPosition);
                if (validMove == null) {
                    break;
                }
                validMoves.add(validMove);
                if (board.getPiece(validMove.getEndPosition()) != null) {
                    break;
                }
                newRowIndex -= 1;
                newColIndex += 1;
            }
        }
        else if (piece.type == PieceType.KING) {
            // can move any direction but only one space
        }
        else if (piece.type == PieceType.KNIGHT) {
            // 2 down, 1 left
            if (isInBounds(rowIndex - 2, colIndex - 1)) {
                ChessMove validMove = validateMove(board, rowIndex - 2, colIndex - 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            // 2 down, 1 right
            if (isInBounds(rowIndex - 2, colIndex + 1)) {
                ChessMove validMove = validateMove(board, rowIndex - 2, colIndex + 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            // 1 down, 2 left
            if (isInBounds(rowIndex - 1, colIndex - 2)) {
                ChessMove validMove = validateMove(board, rowIndex - 1, colIndex - 2, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            // 1 down, 2 right
            if (isInBounds(rowIndex - 1, colIndex + 2)) {
                ChessMove validMove = validateMove(board, rowIndex - 1, colIndex + 2, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            // 2 up, 1 left
            if (isInBounds(rowIndex + 2, colIndex - 1)) {
                ChessMove validMove = validateMove(board, rowIndex + 2, colIndex - 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            // 2 up, 1 right
            if (isInBounds(rowIndex + 2, colIndex + 1)) {
                ChessMove validMove = validateMove(board, rowIndex + 2, colIndex + 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            // 1 up, 2 left
            if (isInBounds(rowIndex + 1, colIndex - 2)) {
                ChessMove validMove = validateMove(board, rowIndex + 1, colIndex - 2, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            // 1 up, 2 right
            if (isInBounds(rowIndex + 1, colIndex + 2)) {
                ChessMove validMove = validateMove(board, rowIndex + 1, colIndex + 2, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
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

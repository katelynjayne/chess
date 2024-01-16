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

    private Collection<ChessMove> groovyMoves(int rowIndex, int colIndex, int rowDirection, int colDirection, ChessBoard board, ChessPosition position) {
        int newRowIndex = rowIndex + rowDirection;
        int newColIndex = colIndex + colDirection;
        Collection<ChessMove> validMoves = new ArrayList<>();
        while (isInBounds(newRowIndex, newColIndex)) {
            ChessMove validMove = validateMove(board, newRowIndex, newColIndex, position);
            if (validMove == null) {
                break;
            }
            validMoves.add(validMove);
            if (board.getPiece(validMove.getEndPosition()) != null) {
                break;
            }
            newRowIndex += rowDirection;
            newColIndex += colDirection;
        }
        return validMoves;
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
            validMoves.addAll(groovyMoves(rowIndex, colIndex, 1, -1, board, myPosition));
            // up and right
            validMoves.addAll(groovyMoves(rowIndex, colIndex, 1, 1, board, myPosition));
            // down and left
            validMoves.addAll(groovyMoves(rowIndex, colIndex, -1, -1, board, myPosition));
            // down and right
            validMoves.addAll(groovyMoves(rowIndex, colIndex, -1, 1, board, myPosition));
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
            // up and left
            validMoves.addAll(groovyMoves(rowIndex, colIndex, 1, -1, board, myPosition));
            // up and right
            validMoves.addAll(groovyMoves(rowIndex, colIndex, 1, 1, board, myPosition));
            // down and left
            validMoves.addAll(groovyMoves(rowIndex, colIndex, -1, -1, board, myPosition));
            // down and right
            validMoves.addAll(groovyMoves(rowIndex, colIndex, -1, 1, board, myPosition));
            // up
            validMoves.addAll(groovyMoves(rowIndex,colIndex,1,0,board,myPosition));
            // down
            validMoves.addAll(groovyMoves(rowIndex,colIndex,-1,0,board,myPosition));
            // left
            validMoves.addAll(groovyMoves(rowIndex,colIndex,0,-1,board,myPosition));
            // right
            validMoves.addAll(groovyMoves(rowIndex,colIndex,0,1,board,myPosition));
        }
        else if (piece.type == PieceType.ROOK) {
            // up
            validMoves.addAll(groovyMoves(rowIndex,colIndex,1,0,board,myPosition));
            // down
            validMoves.addAll(groovyMoves(rowIndex,colIndex,-1,0,board,myPosition));
            // left
            validMoves.addAll(groovyMoves(rowIndex,colIndex,0,-1,board,myPosition));
            // right
            validMoves.addAll(groovyMoves(rowIndex,colIndex,0,1,board,myPosition));

        }
        return validMoves;
    }
}

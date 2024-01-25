package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
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

    private Boolean isInBounds(int rowIndex, int colIndex) {
       return rowIndex >= 0 && rowIndex < 8 && colIndex < 8 && colIndex >= 0;
    }
    private Boolean sameColorPiece(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) == null) {
            return false;
        }
        return board.getPiece(position).getTeamColor() == color;
    }

    private Boolean differentColorPiece(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) == null) {
            return false;
        }
        return board.getPiece(position).getTeamColor() != color;
    }

    private ChessMove validateMove(ChessBoard board, int rowIndex, int colIndex, ChessPosition currentPosition) {
        ChessPosition newPosition = new ChessPosition(rowIndex + 1, colIndex + 1);
        if (!sameColorPiece(board, newPosition)) {
            return new ChessMove(currentPosition, newPosition);
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

    private Collection<ChessMove> promotionMoves (ChessPosition currentPosition, ChessPosition newPosition) {
        Collection<ChessMove> promotionMoves = new ArrayList<>();
        ChessMove promotionMove1 = new ChessMove(currentPosition, newPosition, PieceType.ROOK);
        ChessMove promotionMove2 = new ChessMove(currentPosition, newPosition, PieceType.KNIGHT);
        ChessMove promotionMove3 = new ChessMove(currentPosition, newPosition, PieceType.BISHOP);
        ChessMove promotionMove4 = new ChessMove(currentPosition, newPosition, PieceType.QUEEN);
        promotionMoves.add(promotionMove1);
        promotionMoves.add(promotionMove2);
        promotionMoves.add(promotionMove3);
        promotionMoves.add(promotionMove4);
        return promotionMoves;
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
        return "ChessPiece{" +
                "color=" + color +
                ", type=" + type +
                '}';
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int rowIndex = myPosition.getRowIndex();
        int colIndex = myPosition.getColIndex();
        Collection<ChessMove> validMoves = new HashSet<>();
        if (type == PieceType.BISHOP) {
            // up and left
            validMoves.addAll(groovyMoves(rowIndex, colIndex, 1, -1, board, myPosition));
            // up and right
            validMoves.addAll(groovyMoves(rowIndex, colIndex, 1, 1, board, myPosition));
            // down and left
            validMoves.addAll(groovyMoves(rowIndex, colIndex, -1, -1, board, myPosition));
            // down and right
            validMoves.addAll(groovyMoves(rowIndex, colIndex, -1, 1, board, myPosition));
        }
        else if (type == PieceType.KING) {
            if (isInBounds(rowIndex + 1, colIndex - 1)) {
                ChessMove validMove = validateMove(board, rowIndex + 1, colIndex - 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            if (isInBounds(rowIndex + 1, colIndex)) {
                ChessMove validMove = validateMove(board, rowIndex + 1, colIndex, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            if (isInBounds(rowIndex + 1, colIndex + 1)) {
                ChessMove validMove = validateMove(board, rowIndex + 1, colIndex + 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            if (isInBounds(rowIndex, colIndex - 1)) {
                ChessMove validMove = validateMove(board, rowIndex, colIndex - 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            if (isInBounds(rowIndex, colIndex + 1)) {
                ChessMove validMove = validateMove(board, rowIndex, colIndex + 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            if (isInBounds(rowIndex - 1, colIndex - 1)) {
                ChessMove validMove = validateMove(board, rowIndex - 1, colIndex - 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            if (isInBounds(rowIndex - 1, colIndex)) {
                ChessMove validMove = validateMove(board, rowIndex - 1, colIndex, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
            if (isInBounds(rowIndex - 1, colIndex + 1)) {
                ChessMove validMove = validateMove(board, rowIndex - 1, colIndex + 1, myPosition);
                if (validMove != null) {
                    validMoves.add(validMove);
                }
            }
        }
        else if (type == PieceType.KNIGHT) {
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
        else if (type == PieceType.PAWN) {
            if (color == ChessGame.TeamColor.WHITE) {
                if (isInBounds(rowIndex + 1, colIndex)) {
                    ChessMove validMove = validateMove(board, rowIndex + 1, colIndex, myPosition);
                    ChessPosition forwardPosition = new ChessPosition(rowIndex + 2, colIndex + 1);
                    if (validMove != null) {
                        if (rowIndex + 1 == 7) {
                            validMoves.addAll(promotionMoves(myPosition, forwardPosition));
                        }
                        else if (board.getPiece(forwardPosition) == null) {
                            validMoves.add(validMove);
                        }
                        if (rowIndex == 1) {
                            ChessMove doubleValidMove=validateMove(board, rowIndex + 2, colIndex, myPosition);
                            ChessPosition doubleForwardPosition=new ChessPosition(rowIndex + 3, colIndex + 1);
                            if (board.getPiece(doubleForwardPosition) == null) {
                                validMoves.add(doubleValidMove);
                            }
                        }
                    }
                }
                if (isInBounds(rowIndex + 1, colIndex - 1)) {
                    ChessPosition diagonalLeft = new ChessPosition(rowIndex + 2, colIndex);
                    if (differentColorPiece(board, diagonalLeft)) {
                        ChessMove validMove = validateMove(board, rowIndex + 1, colIndex - 1, myPosition);
                        if (validMove != null) {
                            if (rowIndex + 1 == 7) {
                                validMoves.addAll(promotionMoves(myPosition, diagonalLeft));
                            }
                            else {
                                validMoves.add(validMove);
                            }
                        }
                    }
                }
                if (isInBounds(rowIndex + 1, colIndex + 1)) {
                    ChessPosition diagonalRight = new ChessPosition(rowIndex + 2, colIndex + 2);
                    if (differentColorPiece(board, diagonalRight)) {
                        ChessMove validMove = validateMove(board, rowIndex + 1, colIndex + 1, myPosition);
                        if (validMove != null) {
                            if (rowIndex + 1 == 7) {
                                validMoves.addAll(promotionMoves(myPosition, diagonalRight));
                            }
                            else {
                                validMoves.add(validMove);
                            }
                        }
                    }
                }
            }
            if (color == ChessGame.TeamColor.BLACK) {
                if (isInBounds(rowIndex - 1, colIndex)) {
                    ChessMove validMove = validateMove(board, rowIndex - 1, colIndex, myPosition);
                    ChessPosition forwardPosition = new ChessPosition(rowIndex, colIndex + 1);
                    if (validMove != null) {
                        if (rowIndex - 1 == 0) {
                            validMoves.addAll(promotionMoves(myPosition, forwardPosition));
                        }
                        else if (board.getPiece(forwardPosition) == null) {
                            validMoves.add(validMove);
                        }
                        if (rowIndex == 6) {
                            ChessMove doubleValidMove = validateMove(board, rowIndex - 2, colIndex, myPosition);
                            ChessPosition doubleForwardPosition = new ChessPosition(rowIndex - 1, colIndex + 1);
                            if (board.getPiece(doubleForwardPosition) == null) {
                                validMoves.add(doubleValidMove);
                            }
                        }
                    }
                }
                if (isInBounds(rowIndex - 1, colIndex - 1)) {
                    ChessPosition diagonalLeft = new ChessPosition(rowIndex, colIndex);
                    if (differentColorPiece(board, diagonalLeft)) {
                        ChessMove validMove = validateMove(board, rowIndex - 1, colIndex - 1, myPosition);
                        if (validMove != null) {
                            if (rowIndex - 1 == 0) {
                                validMoves.addAll(promotionMoves(myPosition, diagonalLeft));
                            }
                            else {
                                validMoves.add(validMove);
                            }
                        }
                    }
                }
                if (isInBounds(rowIndex - 1, colIndex + 1)) {
                    ChessPosition diagonalRight = new ChessPosition(rowIndex, colIndex + 2);
                    if (differentColorPiece(board, diagonalRight)) {
                        ChessMove validMove = validateMove(board, rowIndex - 1, colIndex + 1, myPosition);
                        if (validMove != null) {
                            if (rowIndex - 1 == 0) {
                                validMoves.addAll(promotionMoves(myPosition, diagonalRight));
                            }
                            else {
                                validMoves.add(validMove);
                            }
                        }
                    }
                }
            }
        }
        else if (type == PieceType.QUEEN) {
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
        else if (type == PieceType.ROOK) {
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

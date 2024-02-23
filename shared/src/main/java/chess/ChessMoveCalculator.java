package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class ChessMoveCalculator {
   private final ChessPiece piece;
   private final ChessBoard board;
   private final ChessPosition myPosition;
   private final int rowIndex;
   private final int colIndex;
   private final Collection<ChessMove> validMoves;
   public ChessMoveCalculator(ChessPiece piece, ChessBoard board, ChessPosition myPosition) {
      this.piece = piece;
      this.board = board;
      this.myPosition = myPosition;
      rowIndex = myPosition.getRowIndex();
      colIndex = myPosition.getColIndex();
      validMoves = new HashSet<>();
   }
   private Boolean isInBounds(int rowIndex, int colIndex) {
      return rowIndex >= 0 && rowIndex < 8 && colIndex < 8 && colIndex >= 0;
   }
   private Boolean sameColorPiece(ChessBoard board, ChessPosition position) {
      if (board.getPiece(position) == null) {
         return false;
      }
      return board.getPiece(position).getTeamColor() == piece.getTeamColor();
   }

   private Boolean differentColorPiece(ChessBoard board, ChessPosition position) {
      if (board.getPiece(position) == null) {
         return false;
      }
      return board.getPiece(position).getTeamColor() != piece.getTeamColor();
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
      ChessMove promotionMove1 = new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.ROOK);
      ChessMove promotionMove2 = new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.KNIGHT);
      ChessMove promotionMove3 = new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.BISHOP);
      ChessMove promotionMove4 = new ChessMove(currentPosition, newPosition, ChessPiece.PieceType.QUEEN);
      promotionMoves.add(promotionMove1);
      promotionMoves.add(promotionMove2);
      promotionMoves.add(promotionMove3);
      promotionMoves.add(promotionMove4);
      return promotionMoves;
   }

   public Collection<ChessMove> bishopMoves() {
      // up and left
      validMoves.addAll(groovyMoves(rowIndex, colIndex, 1, -1, board, myPosition));
      // up and right
      validMoves.addAll(groovyMoves(rowIndex, colIndex, 1, 1, board, myPosition));
      // down and left
      validMoves.addAll(groovyMoves(rowIndex, colIndex, -1, -1, board, myPosition));
      // down and right
      validMoves.addAll(groovyMoves(rowIndex, colIndex, -1, 1, board, myPosition));
      return validMoves;
   }

   public Collection<ChessMove> kingMoves() {
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
      return validMoves;
   }


   public Collection<ChessMove> knightMoves() {
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
      return validMoves;
   }

   public Collection<ChessMove> pawnMoves() {
      if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
         if (isInBounds(rowIndex + 1, colIndex)) {
            ChessMove validMove = validateMove(board, rowIndex + 1, colIndex, myPosition);
            ChessPosition forwardPosition = new ChessPosition(rowIndex + 2, colIndex + 1);
            if (validMove != null) {
               if (rowIndex + 1 == 7) { validMoves.addAll(promotionMoves(myPosition, forwardPosition)); }
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
      if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
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
      return validMoves;
   }

   public Collection<ChessMove> queenMoves() {
      bishopMoves();
      rookMoves();
      return validMoves;
   }

   public Collection<ChessMove> rookMoves() {
      // up
      validMoves.addAll(groovyMoves(rowIndex, colIndex, 1, 0, board, myPosition));
      // down
      validMoves.addAll(groovyMoves(rowIndex, colIndex, -1, 0, board, myPosition));
      // left
      validMoves.addAll(groovyMoves(rowIndex, colIndex, 0, -1, board, myPosition));
      // right
      validMoves.addAll(groovyMoves(rowIndex, colIndex, 0, 1, board, myPosition));
      return validMoves;
   }
}

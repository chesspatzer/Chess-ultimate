package com.company;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract class that has common implementations pertaining to a piece, implements IPiece interface
 */
public abstract class PieceImpl implements IPiece {

    private int rowPos;
    private int colPos;
    private Color color;
    private Notation pieceType;
    private int boardSize;
    private Square[][] squares;
    private boolean hasMoved;

    /**
     * PieceImpl constructor, constructs the piece based on the parameters
     * @param color color of the piece
     * @param rowPos row position of piece
     * @param colPos column position of piece
     * @param pieceType type of the piece (P,N,K,R,Q,B)
     * @param boardSize size of the board
     * @param squares 2-Dimentional grid of squares where pieces can be placed
     */
    public PieceImpl(Color color, int rowPos, int colPos, Notation pieceType, int boardSize, Square[][] squares) {
        this.color = color;
        this.rowPos = rowPos;
        this.colPos = colPos;
        this.pieceType = pieceType;
        this.boardSize = boardSize;
        this.squares = squares;
        this.hasMoved = false;
    }

    public int getRowPos() {
        return rowPos;
    }

    public void setRowPos(int rowPos) {
        this.rowPos = rowPos;
    }

    public int getColPos() {
        return colPos;
    }

    public void setColPos(int colPos) {
        this.colPos = colPos;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Notation getPieceType() {
        return pieceType;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Square[][] getSquares() {
        return squares;
    }

    /**
     * Method that checks if the given player is under check/the player's king is attacked or not
     * @param color color of the player who's check status needs to be checked
     * @return boolean that determines if the player is in check
     */
    public boolean isPlayerInCheck(Color color) {
        //gets the king's poisiton
        Pair<Integer, Integer> kingPosition = getKingPosition(color);

        //loops through the squares of the board and finds opponent pieces that could attack the king
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                IPiece piece = this.squares[i][j].getPiece();
                if (piece != null && piece.getColor()!= color) {
                    if (piece.possibleMoves().stream().anyMatch(move -> move.getKey().equals(kingPosition.getKey()) && move.getValue().equals(kingPosition.getValue()))) {
                        //king is under attack
                        return true;
                    }
                }

            }
        }
        //if not piece is attacking the king, player is not in check
        return false;
    }

    /**
     * Method that checks if the given player has any move or not
     * @param color color of the player to be checked for stalemate
     * @return boolean that determines if the player is under stalemate
     */
    public boolean isPlayerStalemated(Color color) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                IPiece piece = squares[i][j].getPiece();
                if (piece != null && piece.getColor().equals(color) && !piece.safeMoves().isEmpty()) {
                    System.out.println(piece.getPieceType() + " " + " ");
                    piece.safeMoves().forEach(move -> System.out.println(move.getKey() + " " + move.getValue()));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method that if the player is under checkmate (game over)
     * @param color color of the player to be checked for checkmate
     * @return boolean that determines if the player is under checkmate
     */
    public boolean isPlayerCheckmated(Color color) {
        return isPlayerInCheck(color) && (isPlayerStalemated(color));
    }

    public Pair<Integer, Integer> getKingPosition(Color color) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (squares[i][j].getPiece() != null && squares[i][j].getPiece().getColor().equals(color) && squares[i][j].getPiece().getPieceType() == Notation.K) {
                    return new Pair<>(i, j);
                }
            }
        }
        return null;
    }

    @Override
    public List<Pair<Integer, Integer>> safeMoves() {
        int originalRow = getRowPos();
        int originalCol = getColPos();

        Square originalSquare = getSquares()[originalRow][originalCol];

        List<Pair<Integer, Integer>> finalSafeMovesList = new ArrayList<>();

        for (Pair<Integer, Integer> possibleMove : possibleMoves()) {
            int row = possibleMove.getKey();
            int col = possibleMove.getValue();
            Pair<Character, Integer> position = rowColToFileRank(row, col);
            char file = position.getKey();
            int rank = position.getValue();
            Square finalSquare = getSquares()[row][col];
            IPiece piece = finalSquare.getPiece();
            boolean move = move(file, rank, true);
            if (move) {
                originalSquare.setPiece(null);
                finalSquare.setPiece(this);

                //temporarily move the piece and check if the king is in check then reset the position by undoing the move

                if (!isPlayerInCheck(getColor())) {
                    finalSafeMovesList.add(possibleMove);
                }
                setRowPos(originalRow);
                setColPos(originalCol);
                finalSquare.setPiece(piece);
                originalSquare.setPiece(this);
            }
        }
        return finalSafeMovesList;
    }

    /**
     * convertor, used to convert files and ranks to columns and rows
     * @param file chess terminology for each column (A....H)
     * @param rank chess terminollogy for each row (1....8)
     * @return pair of integers having the converted row and column positions
     */
    public Pair<Integer, Integer> fileRankToRowCol(char file, int rank) {
        int row = rank - 1;
        int col = (getBoardSize() - 1) - (file - 97);
        return new Pair<>(row, col);
    }

    /**
     * convertor, used to convert rows and columns to files and ranks
     * @param rowPos row position
     * @param colPos column position
     * @return pair of values that contains converted files and ranks
     */
    public Pair<Character, Integer> rowColToFileRank(int rowPos, int colPos) {
        char file = (char) ((getBoardSize() - 1) - colPos + 97);
        int rank = rowPos + 1;
        return new Pair<>(file, rank);
    }

    /**
     * checks the bounds, and returns if the file and rank positions
     * @param file
     * @param rank
     * @return
     */
    public boolean checkBounds(char file, int rank) {
        Pair<Integer, Integer> rowCol = fileRankToRowCol(file, rank);
        return 0 <= rowCol.getKey() && rowCol.getKey() <= 7 &&  0 <= rowCol.getValue() && rowCol.getValue() <= 7;
//        return 0 <= file - 97 && file - 97 < getBoardSize() && 1 <= rank && rank <= getBoardSize();
    }

    @Override
    public boolean hasMoved() {
        return hasMoved;
    }


    @Override
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public void display(){
        String[][] array = new String[8][8];
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = boardSize - 1; j >= 0; j--) {
                array[i][j] = " □ ";
                array[i][j] = squares[i][j].getColor().equals(Color.BLACK) ? Board.GREEN + array[i][j] + Board.RESET : array[i][j];
                array[i][j] = squares[i][j].getColor().equals(Color.BLACK) ? Board.GREEN + array[i][j] + Board.RESET : array[i][j];
                if (squares[i][j].getPiece() != null) {
                    array[i][j] = squares[i][j].getPiece().getColor().equals(Color.BLACK) ? Board.BLACK + " " + squares[i][j].getPiece().getPieceType() + " " + Board.RESET : Board.YELLOW + " " + squares[i][j].getPiece().getPieceType() + " " + Board.RESET;
                }
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

}

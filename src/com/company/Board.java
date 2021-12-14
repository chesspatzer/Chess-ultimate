package com.company;

import com.company.pieces.*;


/**
 * Board class that consists of the squares and the players
 */
public class Board {

    private Square[][] squares; //squares of the board
    private Player p1; //player 1
    private Player p2; //player 2
    private int boardSize; //board grid size

    //Colors

    // Color Reset
    public static final String RESET = "\033[0m";

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE


    /**
     * Board constructor that constructs the board
     * @param p1 player 1
     * @param p2 player 2
     * @param boardSize size of the board
     */
    public Board(Player p1, Player p2, int boardSize) {
        this.p1 = p1;
        this.p2 = p2;
        this.boardSize = boardSize;
        squares = new Square[boardSize][boardSize];
        resetBoard();
        p1.setBoard(this);
        p2.setBoard(this);

    }

    /**
     * void method that sets the board and its squares with the proper colors
     */
    void resetBoard() {
        boolean isWhite = true;
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                Color color = isWhite ? Color.WHITE : Color.BLACK;
                this.squares[row][col] = new Square(color, row + 1, (char) (97 + squares[row].length-1 - col));
                isWhite = !isWhite; //alternating square colors
            }
            isWhite = !isWhite; //reversing the alternating square color cause end row square color and starting row square color is the same
        }
        populatePieces();
    }

    /**
     * method to populate all the pieces with their initial positions
     */
    void populatePieces() {
        King.initialPositions.forEach(pos -> {
            int col = boardSize-1 - (pos.getKey() - 97);
            int row = pos.getValue() - 1;
            Color c = (row == 0) ? Color.WHITE : Color.BLACK;
            King k = new King(row, col, c, squares);
            this.squares[row][col].setPiece(k);
        });

        Queen.initialPositions.forEach(pos -> {
            int col = boardSize-1 - (pos.getKey() - 97);
            int row = pos.getValue() - 1;
            Color c = (row == 0) ? Color.WHITE : Color.BLACK;
            Queen q = new Queen(row, col, c, squares);
            this.squares[row][col].setPiece(q);
        });

        Bishop.initialPositions.forEach(pos -> {
            int col = boardSize-1 - (pos.getKey() - 97);
            int row = pos.getValue() - 1;
            Color c = (row == 0) ? Color.WHITE : Color.BLACK;
            Bishop b = new Bishop(row, col, c, squares);
            this.squares[row][col].setPiece(b);
        });

        Rook.initialPositions.forEach(pos -> {
            int col = boardSize-1 - (pos.getKey() - 97);
            int row = pos.getValue() - 1;
            Color c = (row == 0) ? Color.WHITE : Color.BLACK;
            Rook r = new Rook(row, col, c, squares);
            this.squares[row][col].setPiece(r);
        });

        Knight.initialPositions.forEach(pos -> {
            int col = boardSize-1 - (pos.getKey() - 97);
            int row = pos.getValue() - 1;
            Color c = (row == 0) ? Color.WHITE : Color.BLACK;
            Knight k = new Knight(row, col, c, squares);
            this.squares[row][col].setPiece(k);
        });

        //initializing pawns
        for (int i = 0; i < squares[0].length; i++) {
            Pawn whitePawn = new Pawn(1, i, Color.WHITE, squares);
            squares[1][i].setPiece(whitePawn);
            Pawn blackPawn = new Pawn(squares.length - 2, i, Color.BLACK, squares);
            squares[squares.length - 2][i].setPiece(blackPawn);
        }

    }

    /**
     * getter method gets the squares of the board
     * @return 2D array of squares
     */
    public Square[][] getSquares() {
        return squares;
    }

    /**
     * method used for debugging, prints a normal chess board with white on top, black on the bottom
     */
    public void displayNormalBoard() {
        String[][] array = new String[8][8];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                array[i][j] = " 0 ";
                array[i][j] = squares[i][j].getColor().equals(Color.BLACK) ? GREEN + array[i][j] + RESET : array[i][j];
                array[i][j] = squares[i][j].getColor().equals(Color.BLACK) ? GREEN + array[i][j] + RESET : array[i][j];
                if (squares[i][j].getPiece() != null) {
                    array[i][j] = squares[i][j].getPiece().getColor().equals(Color.BLACK) ? BLACK + " " + squares[i][j].getPiece().getPieceType() + " " + RESET : YELLOW + " " + squares[i][j].getPiece().getPieceType() + " " + RESET;
                }
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * method used for debugging, prints a normal chess board with white on the bottom and black on the top
     */
    public void displayBoard() {
        String[][] array = new String[8][8];
        for (int i = boardSize - 1; i >= 0; i--) {
            for (int j = boardSize - 1; j >= 0; j--) {
                array[i][j] = " □ ";
                array[i][j] = squares[i][j].getColor().equals(Color.BLACK) ? GREEN + array[i][j] + RESET : array[i][j];
                array[i][j] = squares[i][j].getColor().equals(Color.BLACK) ? GREEN + array[i][j] + RESET : array[i][j];
                if (squares[i][j].getPiece() != null) {
                    array[i][j] = squares[i][j].getPiece().getColor().equals(Color.BLACK) ? BLACK + " " + squares[i][j].getPiece().getPieceType() + " " + RESET : YELLOW + " " + squares[i][j].getPiece().getPieceType() + " " + RESET;
                }
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * method that checks if the given file and rank values are within bounds of the board
     *
     * @param file chess terminology for each column (A....H)
     * @param rank chess terminollogy for each row (1....8)
     **/
    public boolean checkBounds(char file, int rank) {
        return 0 <= file - 97 && file - 97 < boardSize && 1 <= rank && rank <= boardSize ;
    }


    /**
     * method that gets the size of the board
     * @return
     */
    public int getBoardSize() {
        return boardSize;
    }


    /**
     * method that returns player1 (white)
     * @return player 1, white
     */
    public Player getP1() {
        return p1;
    }

    /**
     * method that returns player 2 (black)
     * @return player 2, black
     */
    public Player getP2() {
        return p2;
    }
}

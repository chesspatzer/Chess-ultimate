package com.company;

import javafx.util.Pair;

import java.util.List;

/**
 * Piece interface that has methods pertaining to each piece
 */
public interface IPiece {

    /**
     * Move method that returns true if the move is valid and if valid, removes the piece from current location and places it in the destination spot
     *
     * @param file chess terminology for each column (A....H)
     * @param rank chess terminollogy for each row (1....8)
     * @param isTempMove checks if the move is temporary, if it is, it doesnt make any changes to the piece properties
     * @return boolean value that lets us know if the move can happen
     */
    boolean move(char file, int rank, boolean isTempMove);

    /**
     * method to check if a given move is legal or not
     *
     * @param file chess terminology for each column (A....H)
     * @param rank chess terminollogy for each row (1....8)
     * @return boolean value, if destination is a legal move
     */
    boolean checkLegality(char file, int rank);

    /**
     * gets the Row Position of the piece
     * @return piece Row Position
     */
    int getRowPos();

    /**
     * sets the Row Position of the piece
     * @param rowPos Row Position to place the piece at
     */
    void setRowPos(int rowPos);

    /**
     * gets the Column Position of the piece
     * @return piece Column Position
     */
    int getColPos();

    /**
     * sets the Column Position of the piece
     * @param colPos Column Position to place the piece at
     */
    void setColPos(int colPos);

    /**
     * gets the Color (WHITE/BLACK) of the particular piece
     * @return piece Color
     */
    Color getColor();

    /**
     * gets the type of piece, its notation
     * @return piece type notation enum
     */
    Notation getPieceType();

    /**
     * List of possible moves that can be made by the piece
     * @return list of pairs, containing row and column positions
     */
    List<Pair<Integer, Integer>> possibleMoves();

    /**
     * List of possible moves that can be made by the piece safely(without check), considers pinned pieces too
     * @return list of pairs, containing row and column positions
     */
    List<Pair<Integer,Integer>> safeMoves();

    /**
     * Method that checks if some pieces have moved, inorder to accommodate special moves (castling, double pawn push at initial position)
     * @return boolean that checks if the piece has moved
     */
    boolean hasMoved();

    /**
     * void method that sets if the piece has moved
     * @param hasMoved value to be set to the piece if it has/has not moved
     */
    void setHasMoved(boolean hasMoved);

}

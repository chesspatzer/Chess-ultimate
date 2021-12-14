package com.company.pieces;

import com.company.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Knight class that extends PieceImpl abstract class
 */
public class Knight extends PieceImpl {

    //inital positions of the knight
    public final static List<Pair<Character, Integer>> initialPositions = new ArrayList<>(
            Arrays.asList(
                    new Pair<>('b', 1),
                    new Pair<>('g',1),
                    new Pair<>('b', 8),
                    new Pair<>('g',8)
            ));

    /**
     * Knight constructor that constructs the knight piece
     * @param row row position of the knight
     * @param col col position of the knight
     * @param color color of the knight
     * @param squares squares of the board
     */
    public Knight(int row, int col, Color color, Square[][] squares) {
        this(row,col,color, 8, squares);
    }

    /**
     * Similar constructor that has an extra parameter for board size
     * @param row row position of the knight
     * @param col col position of the knight
     * @param color color of the knight
     * @param boardSize size of the board
     * @param squares squares of the board
     */
    public Knight(int row,int col, Color color, int boardSize, Square[][] squares) {
        super(color,row, col, Notation.N, boardSize, squares);
    }

    @Override
    public boolean move(char file, int rank, boolean isTempMove) {
        if (checkLegality(file, rank)) {
            Pair<Integer, Integer> position = fileRankToRowCol(file, rank);
            setRowPos(position.getKey());
            setColPos(position.getValue());
            return true;
        }
        return false;
    }

    @Override
    public boolean checkLegality(char file, int rank) {

        int row = rank-1;
        int col = (getBoardSize() - 1) - (file - 97);

        int colDistance = Math.abs(getColPos() - col);
        int rowDistance = Math.abs(getRowPos() - row);

        if (!((colDistance == 2 && rowDistance == 1) || (colDistance == 1 && rowDistance == 2))){
            return false;
        }

        IPiece pieceIfAtDestination = getSquares()[row][col].getPiece();

        return pieceIfAtDestination == null || !pieceIfAtDestination.getColor().equals(this.getColor());
    }



    public List<Pair<Integer, Integer>> possibleMoves() {
        int candidateRow;
        int candidateCol;

        List<Pair<Integer, Integer>> candidateMoves = new ArrayList<>();

        //Top Right
        candidateRow = getRowPos() - 2;
        candidateCol = getColPos() + 1;

        candidateMoves.add(new Pair<>(candidateRow, candidateCol));

        //Top Left
        candidateRow = getRowPos() - 2;
        candidateCol = getColPos() - 1;

        candidateMoves.add(new Pair<>(candidateRow, candidateCol));

        //Right Top
        candidateRow = getRowPos() - 1;
        candidateCol = getColPos() + 2;

        candidateMoves.add(new Pair<>(candidateRow, candidateCol));

        //Right Bottom
        candidateRow = getRowPos() + 1;
        candidateCol = getColPos() + 2;

        candidateMoves.add(new Pair<>(candidateRow, candidateCol));

        //Bottom Right
        candidateRow = getRowPos() + 2;
        candidateCol = getColPos() + 1;

        candidateMoves.add(new Pair<>(candidateRow, candidateCol));

        //Bottom Left
        candidateRow = getRowPos() + 2;
        candidateCol = getColPos() - 1;

        candidateMoves.add(new Pair<>(candidateRow, candidateCol));


        //Left Top
        candidateRow = getRowPos() - 1;
        candidateCol = getColPos() - 2;

        candidateMoves.add(new Pair<>(candidateRow, candidateCol));

        //Left Bottom
        candidateRow = getRowPos() + 1;
        candidateCol = getColPos() - 2;

        candidateMoves.add(new Pair<>(candidateRow, candidateCol));

        List<Pair<Integer, Integer>> filteredMoves = candidateMoves.stream().filter(candidateMove -> {
            int row = candidateMove.getKey();
            int col = candidateMove.getValue();
            char file = (char) ((getBoardSize() - 1) - col + 97);
            int rank = row + 1;
            return checkBounds(file, rank) && checkLegality(file, rank);
        }).collect(Collectors.toList());

        return filteredMoves;
    }


}

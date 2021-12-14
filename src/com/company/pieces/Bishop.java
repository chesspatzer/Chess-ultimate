package com.company.pieces;

import com.company.*;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bishop class that extends PieceImpl abstract class
 */
public class Bishop extends PieceImpl {

    //initial positions of the bishop
    public final static List<Pair<Character, Integer>> initialPositions = new ArrayList<>(
            Arrays.asList(
                    new Pair<>('c', 1),
                    new Pair<>('c', 8),
                    new Pair<>('f', 1),
                    new Pair<>('f', 8)
            ));


    /**
     * Bishop constructor that constructs the bishop piece
     * @param row row position of the bishop
     * @param col col position of the bishop
     * @param color color of the bishop
     * @param squares squares of the board
     */
    public Bishop(int row, int col, Color color, Square[][] squares) {
        this(row, col, color, 8, squares);
    }


    /**
     * Similar constructor that has an extra parameter for board size
     * @param row row position of the bishop
     * @param col col position of the bishop
     * @param color color of the bishop
     * @param boardSize size of the board
     * @param squares squares of the board
     */
    public Bishop(int row, int col, Color color, int boardSize, Square[][] squares) {
        super(color,row, col, Notation.B, boardSize, squares);
    }


    public List<Pair<Integer, Integer>> possibleMoves() {

        List<Pair<Integer, Integer>> candidateMoves = new ArrayList<>();
        int candidateRow = getRowPos();
        int candidateCol = getColPos();

        while (true) {
            candidateRow = candidateRow + 1;
            candidateCol = candidateCol + 1;
            if (candidateRow > 7 || candidateCol > 7) {
                break;
            }
            candidateMoves.add(new Pair<>(candidateRow, candidateCol));
        }

        candidateRow = getRowPos();
        candidateCol = getColPos();

        while (true) {
            candidateRow = candidateRow - 1;
            candidateCol = candidateCol - 1;
            if (candidateRow < 0 || candidateCol < 0) {
                break;
            }
            candidateMoves.add(new Pair<>(candidateRow, candidateCol));
        }


        candidateRow = getRowPos();
        candidateCol = getColPos();

        while (true) {
            candidateRow = candidateRow - 1;
            candidateCol = candidateCol + 1;

            if (candidateRow < 0 || candidateCol > 7) {
                break;
            }
            candidateMoves.add(new Pair<>(candidateRow, candidateCol));
        }

        candidateRow = getRowPos();
        candidateCol = getColPos();

        while (true) {
            candidateRow = candidateRow + 1;
            candidateCol = candidateCol - 1;

            if (candidateRow > 7 || candidateCol < 0) {
                break;
            }
            candidateMoves.add(new Pair<>(candidateRow, candidateCol));
        }


        List<Pair<Integer, Integer>> filteredMoves = candidateMoves.stream().filter(candidateMove -> {
            int row = candidateMove.getKey();
            int col = candidateMove.getValue();
            char file = (char) ((getBoardSize() - 1) - col + 97);
            int rank = row + 1;
            return checkLegality(file, rank);
        }).collect(Collectors.toList());

        return filteredMoves;

    }


    @Override
    public boolean move(char file, int rank, boolean isTempMove) {
        if (checkLegality(file, rank)){
            Pair<Integer, Integer> position = fileRankToRowCol(file, rank);
            setRowPos(position.getKey());
            setColPos(position.getValue());
            return true;
        }
        return false;
    }

    @Override
    public boolean checkLegality(char file, int rank) {
        Pair<Integer, Integer> position = fileRankToRowCol(file, rank);
        int col = position.getValue();
        int row = position.getKey();

        IPiece pieceIfAtDestination = getSquares()[row][col].getPiece();

        if (pieceIfAtDestination!=null && pieceIfAtDestination.getColor().equals(this.getColor())){
            return false;
        }

        if (!(Math.abs(getRowPos()-(rank-1)) == Math.abs(getColPos() - ((getBoardSize()-1) - (file-97))))){
            return false;
        }

        Direction direction = null;

        if (row > getRowPos() && col > getColPos()) {
            direction = Direction.SOUTH_EAST;
        }
        if (row < getRowPos() && col < getColPos()) {
            direction = Direction.NORTH_WEST;
        }
        if (row > getRowPos() && col < getColPos()) {
            direction = Direction.SOUTH_WEST;
        }
        if (row < getRowPos() && col > getColPos()) {
            direction = Direction.NORTH_EAST;
        }

        if (direction == null) {
            return false;
        }

        int dist = Math.abs(getRowPos()-row);

        switch (direction){
            case SOUTH_EAST:
                for (int i = 1; i < dist; i++) {
                    IPiece checkPiece = getSquares()[getRowPos()+i][getColPos()+i].getPiece();
                    if (checkPiece!=null) return false;
                }
                break;

            case NORTH_WEST:
                for (int i = 1; i < dist; i++) {
                    IPiece checkPiece = getSquares()[getRowPos()-i][getColPos()-i].getPiece();
                    if (checkPiece!=null) return false;
                }
                break;

            case SOUTH_WEST:
                for (int i = 1; i < dist; i++) {
                    IPiece checkPiece = getSquares()[getRowPos()+i][getColPos()-i].getPiece();
                    if (checkPiece!=null) return false;
                }
                break;

            case NORTH_EAST:
                for (int i = 1; i < dist; i++) {
                    IPiece checkPiece = getSquares()[getRowPos()-i][getColPos()+i].getPiece();
                    if (checkPiece!=null) return false;
                }
                break;

            default:
                return false;
        }

        return true;
    }


}

package com.company.pieces;

import com.company.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rook class that extends PieceImpl abstract class
 */
public class Rook extends PieceImpl {

    //initial positions of the rook
    public final static List<Pair<Character, Integer>> initialPositions = new ArrayList<>(
            Arrays.asList(
                    new Pair<>('a', 1),
                    new Pair<>('a', 8),
                    new Pair<>('h', 1),
                    new Pair<>('h', 8)
            ));

    /**
     * Rook constructor that constructs the rook piece
     * @param row row position of the rook
     * @param col col position of the rook
     * @param color color of the rook
     * @param squares squares of the board
     */
    public Rook(int row, int col, Color color, Square[][] squares) {
        this(row,col,color,8, squares);
    }

    /**
     * Similar constructor that has an extra parameter for board size
     * @param row row position of the rook
     * @param col col position of the rook
     * @param color color of the rook
     * @param boardSize size of the board
     * @param squares squares of the board
     */
    public Rook(int row, int col, Color color, int boardSize, Square[][] squares) {
        super(color,row, col, Notation.R, boardSize, squares);

    }



    @Override
    public boolean move(char file, int rank, boolean isTempMove) {
        if (checkLegality(file, rank)) {
            Pair<Integer, Integer> position = fileRankToRowCol(file, rank);
            setRowPos(position.getKey());
            setColPos(position.getValue());
            if (!isTempMove){
                setHasMoved(true);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean checkLegality(char file, int rank) {
        int row = rank - 1;
        int col = (getBoardSize()-1) - (file-97);

        IPiece pieceIfAtDestination = getSquares()[row][col].getPiece();

        if (pieceIfAtDestination!=null && pieceIfAtDestination.getColor().equals(this.getColor())){
            return false;
        }

        Direction direction = null;

        if (row > getRowPos()) {
            direction = Direction.SOUTH;
        }
        if (row < getRowPos()) {
            direction = Direction.NORTH;
        }
        if (col > getColPos()) {
            direction = Direction.EAST;
        }
        if (col < getColPos()) {
            direction = Direction.WEST;
        }

        if (direction == null) {
            return false;
        }

        int dist = 0;
        switch (direction){
            case SOUTH:
                dist = Math.abs(row - getRowPos());
                for (int i = 1; i < dist; i++) {
                    IPiece checkPiece = getSquares()[getRowPos()+i][getColPos()].getPiece();
                    if (checkPiece!=null) return false;
                }
                break;
            case NORTH:
                dist = Math.abs(row - getRowPos());
                for (int i = 1; i <dist; i++) {
                    IPiece checkPiece = getSquares()[getRowPos()-i][getColPos()].getPiece();
                    if (checkPiece!=null) return false;
                }
                break;
            case EAST:
                dist = Math.abs(col - getColPos());
                for (int i = 1; i <dist; i++) {
                    IPiece checkPiece = getSquares()[getRowPos()][getColPos()+i].getPiece();
                    if (checkPiece!=null) return false;
                }
                break;
            case WEST:
                dist = Math.abs(col - getColPos());
                for (int i = 1; i <dist; i++) {
                    IPiece checkPiece = getSquares()[getRowPos()][getColPos()-i].getPiece();
                    if (checkPiece!=null) return false;
                }
                break;
            default:
                return false;
        }

        return getRowPos() == rank - 1 || getColPos() == ((getBoardSize() - 1) - (file - 97));
    }



    @Override
    public List<Pair<Integer, Integer>> possibleMoves() {

        List<Pair<Integer, Integer>> candidateMoves = new ArrayList<>();
        int candidateRow = getRowPos();
        int candidateCol = getColPos();

        //rightward
        while (true) {
            candidateCol = candidateCol + 1;
            if (candidateCol > 7) {
                break;
            }
            candidateMoves.add(new Pair<>(candidateRow, candidateCol));
        }

        candidateRow = getRowPos();
        candidateCol = getColPos();

        //leftward
        while (true) {
            candidateCol = candidateCol - 1;
            if (candidateCol < 0) {
                break;
            }
            candidateMoves.add(new Pair<>(candidateRow, candidateCol));
        }

        candidateRow = getRowPos();
        candidateCol = getColPos();

        //upward
        while (true) {
            candidateRow = candidateRow - 1;

            if (candidateRow < 0) {
                break;
            }
            candidateMoves.add(new Pair<>(candidateRow, candidateCol));
        }

        candidateRow = getRowPos();
        candidateCol = getColPos();

        //downward
        while (true) {
            candidateRow = candidateRow + 1;

            if (candidateRow > 7) {
                break;
            }
            candidateMoves.add(new Pair<>(candidateRow, candidateCol));
        }


        List<Pair<Integer, Integer>> collect = candidateMoves.stream().filter(candidateMove -> {
            int row = candidateMove.getKey();
            int col = candidateMove.getValue();
            Pair<Character, Integer> position = rowColToFileRank(row, col);
            char file = position.getKey();
            int rank = position.getValue();
            return checkLegality(file, rank);
        }).collect(Collectors.toList());

        return collect;
    }


}

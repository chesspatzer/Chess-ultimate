package com.company.pieces;

import com.company.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Pawn class that extends PieceImpl abstract class
 */
public class Pawn extends PieceImpl {

    /**
     * Pawn constructor that constructs the pawn piece
     * @param row row position of the pawn
     * @param col col position of the pawn
     * @param color color of the pawn
     * @param squares squares of the board
     */

    public Pawn(int row,int col, Color color, Square[][] squares) {
        this(row,col,color,8, squares);
    }


    /**
     * Similar constructor that has an extra parameter for board size
     * @param row row position of the pawn
     * @param col col position of the pawn
     * @param color color of the pawn
     * @param boardSize size of the board
     * @param squares squares of the board
     */
    public Pawn(int row, int col, Color color, int boardSize, Square[][] squares) {
        super(color,row, col, Notation.P, boardSize, squares);

    }


    @Override
    public boolean move(char file, int rank, boolean isTempMove) {
        Square initialSquare = getSquares()[getRowPos()][getColPos()];

        Pair<Integer, Integer> position = fileRankToRowCol(file, rank);

        Square finalSquare = getSquares()[position.getKey()][position.getValue()];

        if (checkLegality(file, rank)) {

            setRowPos(position.getKey());
            setColPos(position.getValue());
            Queen queen = null;
            if (getRowPos()==0 && getColor().equals(Color.BLACK)){
                 queen = new Queen(getRowPos(), getColPos(), Color.BLACK, getSquares());
            }

            if (getRowPos()==7 && getColor().equals(Color.WHITE)){
                 queen = new Queen(getRowPos(), getColPos(), Color.WHITE, getSquares());

            }

            if (!isTempMove){
                setHasMoved(true);
            }

            initialSquare.setPiece(null);
            if (queen!=null) {
                finalSquare.setPiece(queen);
            } else {
                finalSquare.setPiece(this);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean checkLegality(char file, int rank) {
        Pair<Integer, Integer> position = fileRankToRowCol(file, rank);
        int col = position.getValue();
        int row = position.getKey();

        int rowDistance = getColor() == Color.WHITE? (rank-1) - getRowPos() : getRowPos() - (rank-1);
        //pawn directions are different for each color, so handling that

        int colDistance = Math.abs(getColPos() - ((getBoardSize() - 1) - (file - 97)));




        if (!hasMoved() && colDistance == 0 && rowDistance == 2) {
            for (int i = 1; i <= rowDistance; i++){
                int shifter = getColor() == Color.WHITE? i : -i;
                IPiece checkPiece = getSquares()[getRowPos()+shifter][getColPos()].getPiece();
                if (checkPiece!=null) return false;
            }
            return true;
        }


        IPiece checkPiece = getSquares()[row][col].getPiece();

        if (colDistance == 0 && rowDistance == 1) {
            if (checkPiece!=null) return false;
            return true;
        }

        if (colDistance ==1 && rowDistance ==1) {
            if (checkPiece!=null) {
                return !checkPiece.getColor().equals(getColor());
            } else {
                return false;
            }
        }
        return false;

    }


    @Override
    public List<Pair<Integer, Integer>> possibleMoves() {
        Direction direction = getColor() == Color.WHITE? Direction.SOUTH : Direction.NORTH;
        List<Pair<Integer,Integer>> candidateMoves = new ArrayList<>();

        switch (direction){
            case SOUTH:
                for (int i = 1; i <= 2; i++){
                    candidateMoves.add(new Pair<>(getRowPos()+i, getColPos()));
                    candidateMoves.add(new Pair<>(getRowPos()+i, getColPos()-1));
                    candidateMoves.add(new Pair<>(getRowPos()+i, getColPos()-1));
                }
                break;
            case NORTH:
                for (int i = 1; i <= 2; i++){
                    candidateMoves.add(new Pair<>(getRowPos()-i, getColPos()));
                    candidateMoves.add(new Pair<>(getRowPos()-i, getColPos()-1));
                    candidateMoves.add(new Pair<>(getRowPos()-i, getColPos()-1));
                }
                break;
        }

        List<Pair<Integer, Integer>> filteredMoves = candidateMoves.stream().filter(candidateMove -> {
            int row = candidateMove.getKey();
            int col = candidateMove.getValue();
            Pair<Character, Integer> position = rowColToFileRank(row, col);
            char file = position.getKey();
            int rank = position.getValue();
            return checkBounds(file, rank) && checkLegality(file, rank);
        }).collect(Collectors.toList());

        return filteredMoves;
    }



}

package com.company.pieces;

import com.company.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * King class that extends PieceImpl abstract class
 */
public class King extends PieceImpl {

    //inital positions of the king
    public final static List<Pair<Character, Integer>> initialPositions = new ArrayList<>(
            Arrays.asList(
                    new Pair<>('e', 1),
                    new Pair<>('e', 8)
            ));

    //castling positions of a typical king piece
    public final static List<Pair<Character, Integer>> castlingPositions = new ArrayList<>(
            Arrays.asList(
                    new Pair<>('c', 1),
                    new Pair<>('c', 8),
                    new Pair<>('g', 1),
                    new Pair<>('g', 8)
            ));

    /**
     * King constructor that constructs the king piece
     * @param row row position of the king
     * @param col col position of the king
     * @param color color of the king
     * @param squares squares of the board
     */
    public King(int row, int col, Color color, Square[][] squares) {
        this(row, col, color, 8, squares);
    }

    /**
     * Similar constructor that has an extra parameter for board size
     * @param row row position of the king
     * @param col col position of the king
     * @param color color of the king
     * @param boardSize size of the board
     * @param squares squares of the board
     */
    public King(int row, int col, Color color, int boardSize, Square[][] squares) {
        super(color, row, col, Notation.K, boardSize, squares);
    }

    @Override
    public boolean move(char file, int rank, boolean isTempMove) {

        Pair<Integer, Integer> toPosition = fileRankToRowCol(file, rank);
        Pair<Character, Integer> fromPosition = rowColToFileRank(getRowPos(), getColPos());
        boolean currentIsAtStartPosition = initialPositions.stream().filter(initialPosition -> getColor() == Color.WHITE ? initialPosition.getValue() == 1 : initialPosition.getValue() == 8).anyMatch(initialPosition -> initialPosition.getKey() == fromPosition.getKey() && initialPosition.getValue() == fromPosition.getValue());
        boolean destinationIsCastlingPosition = castlingPositions.stream().filter(initialPosition -> getColor() == Color.WHITE ? initialPosition.getValue() == 1 : initialPosition.getValue() == 8).anyMatch(castlingPosition -> castlingPosition.getKey() == file && castlingPosition.getValue() == rank);

        //check if its a castling move and do castling if possible
        if (currentIsAtStartPosition && destinationIsCastlingPosition) {
            if (isTempMove) {
                return true;
            } else {
                if (castling(file, rank, fromPosition)) {
                    setHasMoved(true);
                    return true;
                }
            }
        }

        if (checkLegality(file, rank)) {
            setRowPos(toPosition.getKey());
            setColPos(toPosition.getValue());
            if (!isTempMove) {
                setHasMoved(true);
            }
            return true;
        }
        return false;
    }


    /**
     * castling helper method that tries castling and returns a boolean value if castling is successful
     * @param file file to which the king has to be placed
     * @param rank rank to which the king has to be placed
     * @param fromPosition pair of integers containing the source position
     * @return boolean value that indicates if the castling was successful
     */
    public boolean castling(char file, int rank, Pair<Character, Integer> fromPosition) {
        boolean currentIsAtStartPosition = initialPositions.stream().filter(initialPosition -> getColor() == Color.WHITE ? initialPosition.getValue() == 1 : initialPosition.getValue() == 8).anyMatch(initialPosition -> initialPosition.getKey() == fromPosition.getKey() && initialPosition.getValue() == fromPosition.getValue());
        boolean destinationIsCastlingPosition = castlingPositions.stream().filter(initialPosition -> getColor() == Color.WHITE ? initialPosition.getValue() == 1 : initialPosition.getValue() == 8).anyMatch(castlingPosition -> castlingPosition.getKey() == file && castlingPosition.getValue() == rank);


        if (currentIsAtStartPosition && destinationIsCastlingPosition) {
            if (possibleMoves().stream().anyMatch(possibleMove -> possibleMove.getValue().equals(5) && file == 'c')) {
                setColPos(getColPos() + 2);
                Rook rook = (Rook) getSquares()[getRowPos()][getBoardSize() - 1].getPiece();
                rook.setColPos(getColPos() - 1);
                getSquares()[getRowPos()][getBoardSize() - 1].setPiece(null);
                getSquares()[getRowPos()][getColPos() - 1].setPiece(rook);
                if (isPlayerInCheck(getColor())) {
                    setColPos(getColPos() - 2);
                    rook.setColPos(getBoardSize() - 1);
                    getSquares()[getRowPos()][getColPos() - 1].setPiece(null);
                    getSquares()[getRowPos()][getBoardSize() - 1].setPiece(rook);
                    return false;
                }
            }

            if (possibleMoves().stream().anyMatch(possibleMove -> possibleMove.getValue().equals(1) && file == 'g')) {
                setColPos(getColPos() - 2);
                Rook rook = (Rook) getSquares()[getRowPos()][0].getPiece();
                rook.setColPos(getColPos() + 1);
                getSquares()[getRowPos()][0].setPiece(null);
                getSquares()[getRowPos()][getColPos() + 1].setPiece(rook);
                if (isPlayerInCheck(getColor())) {
                    setColPos(getColPos() + 2);
                    rook.setColPos(0);
                    getSquares()[getRowPos()][getColPos() + 1].setPiece(null);
                    getSquares()[getRowPos()][0].setPiece(rook);
                    return false;
                }
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

        int colDistance = Math.abs(getColPos() - col);
        int rowDistance = Math.abs(getRowPos() - row);
        Pair<Character, Integer> fromPosition = rowColToFileRank(getRowPos(), getColPos());


        if (colDistance == 2 && rowDistance == 0 && !hasMoved()) return true;


        if (!(rowDistance <= 1 && colDistance <= 1)) return false;


        IPiece pieceIfAtDestination = getSquares()[row][col].getPiece();

        if (pieceIfAtDestination != null && pieceIfAtDestination.getColor().equals(this.getColor())) {
            return false;
        }

        return true;
    }


    @Override
    public List<Pair<Integer, Integer>> possibleMoves() {
        List<Pair<Integer, Integer>> candidateMoves = new ArrayList<>();

        Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            candidateMoves.add(new Pair<>(getRowPos() + direction.getY(), getColPos() + direction.getX()));
        }

        if (!hasMoved()) {
            candidateMoves.addAll(castlingMoves());
        }

        List<Pair<Integer, Integer>> filteredMoves = candidateMoves.stream().filter(candidateMove -> {
            int row = candidateMove.getKey();
            int col = candidateMove.getValue();
            if (col == Integer.MIN_VALUE || col == Integer.MAX_VALUE) {
                return true;
            } else {
                Pair<Character, Integer> position = rowColToFileRank(row, col);
                char file = position.getKey();
                int rank = position.getValue();
                return checkBounds(file, rank) && checkLegality(file, rank);
            }

        }).collect(Collectors.toList());

        return filteredMoves;
    }


    /**
     * List of castling moves possible by the piece
     * @return pair of integers indicating the moves possible by castling
     */
    public List<Pair<Integer, Integer>> castlingMoves() {
        List<Pair<Integer, Integer>> castlingMoves = new ArrayList<>();

        if (hasMoved()) {
            return castlingMoves;
        }

        boolean inCheck = false;

        switch (getColor()) {
            case WHITE:

                IPiece whiteRookKingSide = getSquares()[0][0].getPiece();
                IPiece whiteRookQueenSide = getSquares()[0][7].getPiece();

                if (whiteRookKingSide != null && whiteRookKingSide.getPieceType().equals(Notation.R) && whiteRookKingSide.getColor().equals(getColor()) && !whiteRookKingSide.hasMoved()) {
                    if (getSquares()[0][1].getPiece() == null && getSquares()[0][2].getPiece() == null) {
                        setColPos(1);
                        Rook rook = (Rook) getSquares()[0][0].getPiece();
                        rook.setColPos(2);

                        //setting rook
                        getSquares()[0][0].setPiece(null);
                        getSquares()[0][2].setPiece(rook);

                        //setting king
                        getSquares()[0][1].setPiece(this);
                        getSquares()[0][3].setPiece(null);


                        if (isPlayerInCheck(getColor())) {
                            inCheck = true;
                        }

                        //reset the piece positions
                        setColPos(3);
                        rook.setColPos(0);

                        //resetting rook
                        getSquares()[0][0].setPiece(rook);
                        getSquares()[0][2].setPiece(null);

                        //resetting king
                        getSquares()[0][3].setPiece(this);
                        getSquares()[0][1].setPiece(null);

                        //checking intermediate spots if they are in check

                        setColPos(2);
                        getSquares()[0][2].setPiece(this);
                        getSquares()[0][3].setPiece(null);

                        if (isPlayerInCheck(getColor())) {
                            inCheck = true;
                        }

                        setColPos(3);
                        getSquares()[0][3].setPiece(this);
                        getSquares()[0][2].setPiece(null);

                        if (!inCheck) {
                            castlingMoves.add(new Pair<>(0, 1));
                        }
                    }
                }

                if (whiteRookQueenSide != null && whiteRookQueenSide.getPieceType().equals(Notation.R) &&
                        whiteRookQueenSide.getColor().equals(getColor()) && !whiteRookQueenSide.hasMoved()) {
                    if (getSquares()[0][4].getPiece() == null && getSquares()[0][5].getPiece() == null && getSquares()[0][6].getPiece() == null) {

                        setColPos(5);
                        Rook rook = (Rook) getSquares()[0][7].getPiece();
                        rook.setColPos(4);

                        //setting rook
                        getSquares()[0][7].setPiece(null);
                        getSquares()[0][4].setPiece(rook);

                        //setting king
                        getSquares()[0][5].setPiece(this);
                        getSquares()[0][3].setPiece(null);


                        if (isPlayerInCheck(getColor())) {
                            inCheck = true;
                        }

                        //reset the piece positions
                        setColPos(3);
                        rook.setColPos(7);

                        //resetting rook
                        getSquares()[0][7].setPiece(rook);
                        getSquares()[0][4].setPiece(null);

                        //resetting king
                        getSquares()[0][3].setPiece(this);
                        getSquares()[0][5].setPiece(null);

                        //checking intermediate spots if they are in check

                        setColPos(4);
                        getSquares()[0][4].setPiece(this);
                        getSquares()[0][3].setPiece(null);

                        if (isPlayerInCheck(getColor())) {
                            inCheck = true;
                        }

                        setColPos(3);
                        getSquares()[0][3].setPiece(this);
                        getSquares()[0][4].setPiece(null);

                        if (!inCheck) {
                            castlingMoves.add(new Pair<>(0, 5));
                        }
                    }
                }
                break;

            case BLACK:
                IPiece blackRookKingSide = getSquares()[7][0].getPiece();
                IPiece blackRookQueenSide = getSquares()[7][7].getPiece();

                if (blackRookKingSide != null && blackRookKingSide.getPieceType().equals(Notation.R) &&
                        blackRookKingSide.getColor().equals(getColor()) && !blackRookKingSide.hasMoved()) {
                    if (getSquares()[7][1].getPiece() == null && getSquares()[7][2].getPiece() == null) {
                        setColPos(1);
                        Rook rook = (Rook) getSquares()[getRowPos()][0].getPiece();
                        rook.setColPos(2);

                        //setting rook
                        getSquares()[7][0].setPiece(null);
                        getSquares()[7][2].setPiece(rook);

                        //setting king
                        getSquares()[7][1].setPiece(this);
                        getSquares()[7][3].setPiece(null);


                        if (isPlayerInCheck(getColor())) {
                            inCheck = true;
                        }

                        //resetting the pieces
                        setColPos(3);
                        rook.setColPos(0);

                        //resetting rook
                        getSquares()[7][0].setPiece(rook);
                        getSquares()[7][2].setPiece(null);

                        //resetting king
                        getSquares()[7][3].setPiece(this);
                        getSquares()[7][1].setPiece(null);

                        //checking intermediate spots if they are in check

                        setColPos(2);
                        getSquares()[7][2].setPiece(this);
                        getSquares()[7][3].setPiece(null);

                        if (isPlayerInCheck(getColor())) {
                            inCheck = true;
                        }

                        setColPos(3);
                        getSquares()[7][3].setPiece(this);
                        getSquares()[7][2].setPiece(null);

                        if (!inCheck) {
                            castlingMoves.add(new Pair<>(7, 1));
                        }
                    }
                }

                if (blackRookQueenSide != null && blackRookQueenSide.getPieceType().equals(Notation.R) &&
                        blackRookQueenSide.getColor().equals(getColor()) && !blackRookQueenSide.hasMoved()) {
                    if (getSquares()[7][4].getPiece() == null && getSquares()[7][5].getPiece() == null && getSquares()[7][6].getPiece() == null) {
                        setColPos(5);
                        Rook rook = (Rook) getSquares()[getRowPos()][getBoardSize() - 1].getPiece();
                        rook.setColPos(4);

                        //setting rook
                        getSquares()[7][7].setPiece(null);
                        getSquares()[7][4].setPiece(rook);

                        //setting king
                        getSquares()[7][5].setPiece(this);
                        getSquares()[7][3].setPiece(null);


                        if (isPlayerInCheck(getColor())) {
                            inCheck = true;
                        }

                        //resetting the pieces
                        setColPos(3);
                        rook.setColPos(7);

                        //resetting rook
                        getSquares()[7][7].setPiece(rook);
                        getSquares()[7][4].setPiece(null);

                        //resetting king
                        getSquares()[7][3].setPiece(this);
                        getSquares()[7][5].setPiece(null);

                        //checking intermediate spots if they are in check

                        setColPos(4);
                        getSquares()[7][4].setPiece(this);
                        getSquares()[7][3].setPiece(null);

                        if (isPlayerInCheck(getColor())) {
                            inCheck = true;
                        }

                        setColPos(3);
                        getSquares()[7][3].setPiece(this);
                        getSquares()[7][4].setPiece(null);

                        if (!inCheck) {
                            castlingMoves.add(new Pair<>(7, 5));
                        }
                    }
                }
                break;
        }
        return castlingMoves;
    }


}

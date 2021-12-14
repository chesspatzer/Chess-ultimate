package com.company;

import apple.laf.JRSUIConstants;
import javafx.util.Pair;

import java.awt.geom.PathIterator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
    private String name;
    private int eloRating;

    private Color color;
    private boolean inCheck;
    private List<String> movesMade;
    private Board board;
    private Pair<Pair<Integer, Integer>,Pair<Integer, Integer>> lastMove;
    private LinkedList<String> moves = new LinkedList<>();
    private DbConnector dbConnector;

    public Player(String name, int eloRating, Color color) {
        this.name = name;
        this.eloRating = eloRating;
        this.color = color;
        this.dbConnector = new DbConnector();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEloRating() {
        return eloRating;
    }

    public void setEloRating(int eloRating) {
        this.eloRating = eloRating;
    }



    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public boolean addMove(char fromFile, int fromRank, char toFile, int toRank){
        System.out.println("from: " + fromFile + fromRank + " to : " + toFile + toRank);
        boolean initialPos = board.checkBounds(fromFile, fromRank);
        boolean finalPos = board.checkBounds(toFile, toRank);
        if (!(initialPos && finalPos)){
            return false;
        }

        if(fromFile==toFile && fromRank==toRank)
            return false;

        Square initialSquare = board.getSquares()[fromRank - 1][board.getBoardSize()-1 - (fromFile - 97)];
        IPiece piece = initialSquare.getPiece();

        if (piece == null || !piece.getColor().equals(color)) return false;

        if (piece.move(toFile, toRank, false)){
            if (piece.getPieceType()!=Notation.P){
                initialSquare.setPiece(null);
                Square finalSquare = board.getSquares()[toRank - 1][board.getBoardSize()-1 - (toFile - 97)];
                finalSquare.setPiece(piece);
            }

            Pair<Integer, Integer> source = new Pair<>(fromRank - 1, board.getBoardSize() - 1 - (fromFile - 97));
            Pair<Integer, Integer> destination = new Pair<>(toRank - 1, board.getBoardSize() - 1 - (toFile - 97));

            setLastMove(new Pair<>(source, destination));

            Notation pieceType = piece.getPieceType();
            String st = fromFile + String.valueOf(fromRank) + "-" + toFile + String.valueOf(toRank);
            moves.add(st);

            String joinedMoves = String.join(", ", moves);
            try {
                int id = dbConnector.getTopIdStatement("Select id  from GameInfo order by id desc limit 1");
                String colorMoves = getColor() == Color.WHITE?  "whiteMoves" : "blackMove";
                String query = String.format("update GameInfo set %s = '%s' where  id = %s",colorMoves, joinedMoves,String.valueOf(id));
                System.out.println(query);
                dbConnector.executeInsertUpdateStatement(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
             moves.forEach(System.out::println);
            return true;
        }
        else
            return false;

    }

    public Pair<Pair<Integer, Integer>,Pair<Integer, Integer>> getLastMove(){
        return lastMove;
    }

    public void setLastMove(Pair<Pair<Integer, Integer>,Pair<Integer, Integer>> lastMove){
        this.lastMove = lastMove;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<String> moveList(){
        return new ArrayList<>(moves);
    }
}

package com.company;

import javafx.util.Pair;


import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Player class containing the information of the player
 */
public class Player {
    private String name;
    private int eloRating;

    private Color color;
    private Board board;
    private Pair<Pair<Integer, Integer>,Pair<Integer, Integer>> lastMove;
    private LinkedList<String> moves = new LinkedList<>();
    private DbConnector dbConnector;


    /**
     * Player constructor, constructs the player
     * @param name name of the player
     * @param eloRating eloRating of the player
     * @param color color of the player
     */
    public Player(String name, int eloRating, Color color) {
        this.name = name;
        this.eloRating = eloRating;
        this.color = color;
        this.dbConnector = new DbConnector();
    }

    /**
     * getter, gets the name of the player
     * @return player name
     */
    public String getName() {
        return name;
    }

    /**
     * setter, sets the player name
     * @param name name to be set for the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter, gets the Elo rating of the player
     * @return integer value of the player rating
     */
    public int getEloRating() {
        return eloRating;
    }

    /**
     * setter, sets the Elo rating of the player
     * @param eloRating
     */
    public void setEloRating(int eloRating) {
        this.eloRating = eloRating;
    }


    /**
     * getter, gets the color of the player
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * setter, sets the color of the player
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }


    /**
     * method used by the player to make a move
     * @param fromFile source column/file
     * @param fromRank source row/rank
     * @param toFile destination column/file
     * @param toRank destination row/rank
     * @return boolean value that indicates if move is successful
     */
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

    /**To be used for en-passant
     * The Latest move the player
     * @return Pair of pairs comprising source and destination positions
     */
    public Pair<Pair<Integer, Integer>,Pair<Integer, Integer>> getLastMove(){
        return lastMove;
    }

    /**
     * sets the last move of the player
     * @param lastMove Pair of pairs comprising source and destination positions
     */
    public void setLastMove(Pair<Pair<Integer, Integer>,Pair<Integer, Integer>> lastMove){
        this.lastMove = lastMove;
    }

    /**
     * getter, gets the board player is playing at
     * @return
     */
    public Board getBoard() {
        return board;
    }

    /**
     * setter, sets the board the player is playing at
     * @param board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

}

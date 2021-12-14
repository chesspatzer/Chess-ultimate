package com.company;

/**
 * Square class that acts as a holder for each piece position
 */
public class Square {
    private Color color; //color of the square
    private char file; // file of the square
    private int rank; // rank of the square
    private IPiece piece; //piece if any, on the square

    /**
     * Square constructor that constructs the square
     * @param color color of the square
     * @param rank rank/row of the square
     * @param file file/column of the square
     */
    public Square(Color color, int rank,  char file){
        this.color = color;
        this.rank = rank;
        this.file = file;
    }

    /**
     * getter, gets the color of the square
     * @return color of square
     */
    public Color getColor() {
        return color;
    }

    /**
     * setter, sets the color of the square
     * @param color color to be set
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * getter, gets the file of the square
     * @return file of square
     */
    public char getFile() {
        return file;
    }

    /**
     * getter, gets the rank of the square
     * @return rank of square
     */
    public int getRank() {
        return rank;
    }

    /**
     * getter, gets the piece at the square
     * @return piece at square
     */
    public IPiece getPiece() {
        return piece;
    }

    /**
     * setter, sets the piece at the square
     * @param piece piece to be set
     */
    public void setPiece(IPiece piece) {
        this.piece = piece;
    }
}

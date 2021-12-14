package com.company;

/**
 * Noation of each chess piece, also used as markers in GUI
 */
public enum Notation {
    K("♚"), Q("♛"),R("♜"), B("♝"), N("♞"), P("♙");

    private String string;

    /**
     * Enum constructor
     * @param s notation string of the piece
     */
    Notation(String s) {
        this.string = s;
    }

    /**
     * toString override of the piece notation
     * @return
     */
    @Override
    public String toString(){
        return this.string;
    }

}


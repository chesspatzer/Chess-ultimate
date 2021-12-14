package com.company.swing;

import com.company.Color;
import com.company.Square;

import javax.swing.*;
import java.awt.*;

/**
 * A Square component for the GUI which is a button, inherits JButton
 */
public class SquareComponent extends JButton {
        public final static java.awt.Color whiteColor = new java.awt.Color(59,122,87);
        public final static java.awt.Color blackColor = new java.awt.Color(0,0,0);

        private Square sq;

    /**
     * Square Component constructor that constructs the square component with a square composed
     * @param sq square that the component comprises
     */
    public SquareComponent(Square sq){
            this.sq=sq;
            super.setBackground(sq.getColor().equals(Color.WHITE) ? new java.awt.Color(255,253,208) : new java.awt.Color(196,8,16));
            setFont(new Font("Arial",Font.BOLD,60));
            setBorderPainted(false);
            setOpaque(true);
            if(sq.getPiece() !=null) {
                setText(sq.getPiece().getPieceType().toString());
                setForeground(sq.getPiece().getColor().equals(Color.WHITE) ? whiteColor : blackColor);
            }

        }

    /**
     * getter method, gets the square inside the square component
     * @return returns a square
     */
    public Square getSq() {
        return sq;
    }

}

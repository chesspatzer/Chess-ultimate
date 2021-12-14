package com.company.swing;

import com.company.Color;
import com.company.Square;

import javax.swing.*;
import java.awt.*;

public class SquareComponent extends JButton {
        public final static java.awt.Color whiteColor = new java.awt.Color(59,122,87);
        public final static java.awt.Color blackColor = new java.awt.Color(0,0,0);

        private Square sq;

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

    public Square getSq() {
        return sq;
    }

}

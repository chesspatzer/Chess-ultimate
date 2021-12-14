package com.company.swing;


import com.company.*;
import com.company.Color;
import com.company.pieces.King;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * BoardComonent class is a JFrame that renders the chess board.
 * The class also acts as the event handler for onClick event of any square in the board.
 *
 */

public class BoardComponent extends JFrame implements ActionListener {
    private Board board;
    boolean isFromSelected = false;
    boolean isPlayer1=true;
    SquareComponent[][] sqcComps = new SquareComponent[8][8];
    SquareComponent fromSquareComp;
    SquareComponent toSquareComp;
    List<Pair<Integer,Integer>> safeMoves;
    IPiece selPiece;



    public BoardComponent() {
        super("Chess game");
        init();
        Image icon = Toolkit.getDefaultToolkit().getImage("src/com/company/resources/chess_logo.jpeg");
        this.setIconImage(icon);
    }

    private void init() {
        Player p1 = new Player("Bob", 1200, Color.WHITE);
        Player p2 = new Player("Pop", 1300,  Color.BLACK);
        board = new Board(p1, p2, 8);

        DbConnector connector = new DbConnector();


        try {
            String query = String.format("insert into GameInfo(white, black,result,whiteMoves,blackMove) values('%s','%s',%s,%s,%s);",p1.getName(),p2.getName(),null,null,null);
//            System.out.println(query);
            connector.executeInsertUpdateStatement(query);
//            int id = connector.getTopIdStatement("Select id top 1 from GameInfo order by id desc");
//             query = String.format("insert into GameInfo(white, black,result,whiteMoves,blackMove) values('%s','%s',%s,%s,%s);",p1.getName(),p2.getName(),null,null,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        isFromSelected=false;
        isPlayer1=true;

        JPanel jPanel = new JPanel(new GridLayout(8, 8, 0, 0));
        jPanel.setOpaque(true);
        add(jPanel, BorderLayout.CENTER);

        for (int i = board.getBoardSize() - 1; i >= 0; --i) {
            for (int j = board.getBoardSize() - 1; j >= 0; --j) {
                boolean isWhite = true;
                sqcComps[i][j] =  new SquareComponent(board.getSquares()[i][j]);

               // SquareComponent squareComponent = new SquareComponent(board.getSquares()[i][j]);
              //  squareComponent.addActionListener(this);
                sqcComps[i][j].addActionListener(this);
               // jPanel.add(squareComponent);
                jPanel.add(sqcComps[i][j]);
            }
        }

        JButton reset = new JButton("RESET");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                init();
            }
        });

        add(reset,BorderLayout.SOUTH);


        setVisible(true);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BoardComponent();
            }
        });
    }

    /**
     * The following method handles the event pertaining to the click of the SquareComponent.
     * Basically, the method colors possible moves and performs a successful move if one is found.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        SquareComponent sqc = (SquareComponent) e.getSource();

        Player p = isPlayer1 ? board.getP1() : board.getP2();
        Color playerColor = p.getColor();

        if (!isFromSelected) {
           if(sqc.getSq().getPiece()==null || sqc.getSq().getPiece().getColor()!=playerColor) {
               return;
           }


            fromSquareComp = sqc;
            selPiece = fromSquareComp.getSq().getPiece();
            fromSquareComp.setBackground(new java.awt.Color(219, 175, 36));


            safeMoves = fromSquareComp.getSq().getPiece().safeMoves();

            highlightSafeMoves();

            isFromSelected = true;
        }
        else {
            if(sqc==fromSquareComp){
                isFromSelected=false;
                java.awt.Color color=  fromSquareComp.getSq().getColor()==Color.WHITE  ? new java.awt.Color(255,253,208) : new java.awt.Color(196,8,16);
                fromSquareComp.setBackground(color);


                deHighlightSafemoves();

                sqc=null;
                return;
            }

            if(sqc!= null && sqc.getSq().getPiece()!=null && sqc.getSq().getPiece().getColor() == playerColor){

                java.awt.Color color=  fromSquareComp.getSq().getColor()==Color.WHITE  ? new java.awt.Color(255,253,208) : new java.awt.Color(196,8,16);
                fromSquareComp.setBackground(color);


                fromSquareComp = sqc;
                selPiece = fromSquareComp.getSq().getPiece();
                fromSquareComp.setBackground(new java.awt.Color(219, 175, 36));


                deHighlightSafemoves();

                safeMoves = fromSquareComp.getSq().getPiece().safeMoves();

                highlightSafeMoves();

                return;
            }

            toSquareComp = sqc;
            boolean moveSuccess = p.addMove(fromSquareComp.getSq().getFile(), fromSquareComp.getSq().getRank(), toSquareComp.getSq().getFile(), toSquareComp.getSq().getRank());
           if (moveSuccess) {

               board.displayNormalBoard();


               for (int i =0; i<8;i++){
                   for (int j=0; j<8; j++){
                       IPiece piece = board.getSquares()[i][j].getPiece();
                       if (piece!=null && piece.getColor().equals(playerColor == Color.WHITE? Color.BLACK: Color.WHITE)) {
                           System.out.println(piece.getPieceType());
                           piece.safeMoves().forEach(move->{
                               System.out.println(move.getKey() + " " + move.getValue());
                           });
                       }

                   }
               }

               safeMoves.forEach(sq -> {
                   int row=sq.getKey();
                   int col=sq.getValue();
                   java.awt.Color colorSq=  board.getSquares()[row][col].getColor()==Color.WHITE  ? new java.awt.Color(255,253,208) : new java.awt.Color(196,8,16);
                   sqcComps[row][col].setBackground(colorSq);});


                String piece= fromSquareComp.getText();
                fromSquareComp.setText("");
                java.awt.Color col=  fromSquareComp.getSq().getColor()==Color.WHITE  ? new java.awt.Color(255,253,208) : new java.awt.Color(196,8,16);
                fromSquareComp.setBackground(col);
                //toSquareComp.setText(piece);
                toSquareComp.setText(toSquareComp.getSq().getPiece().getPieceType().toString());
                toSquareComp.setForeground(playerColor.equals(Color.WHITE) ? SquareComponent.whiteColor : SquareComponent.blackColor);

                //checking if move is castling move
               if(selPiece instanceof King){
                   int locKing = fromSquareComp.getSq().getFile();
                   int finalLocKing = toSquareComp.getSq().getFile();
                   System.out.println(locKing);
                   System.out.println(finalLocKing);

                   if(Math.abs(locKing-finalLocKing) > 1){
                       if(playerColor== Color.WHITE) {
                           if(finalLocKing > locKing) {
                               System.out.println("check 1");
                               System.out.println(board.getSquares()[0][2].getPiece().getPieceType().toString());
                               sqcComps[0][2].setText(board.getSquares()[0][2].getPiece().getPieceType().toString());
                               sqcComps[0][0].setText("");
                           }
                           else {
                               sqcComps[0][4].setText(board.getSquares()[0][4].getPiece().getPieceType().toString());
                               sqcComps[0][7].setText("");
                           }
                       }
                       else {
                           if(finalLocKing > locKing) {
                               sqcComps[7][2].setText(board.getSquares()[7][2].getPiece().getPieceType().toString());
                               sqcComps[7][0].setText("");
                           }
                           else {
                               sqcComps[7][4].setText(board.getSquares()[7][4].getPiece().getPieceType().toString());
                               sqcComps[7][7].setText("");
                           }
                       }
                   }

               }


             if(((PieceImpl)toSquareComp.getSq().getPiece()).isPlayerCheckmated(playerColor==Color.WHITE ? Color.BLACK : Color.WHITE))
             {
                 JOptionPane.showMessageDialog(this, "Player " + playerColor + " wins");

             }

                isPlayer1=!isPlayer1;
                isFromSelected=false;
            }
        }

    }

    /**
     * This method is called when we want to highlight a square in our board to indicate a safe move.
     */

    public void highlightSafeMoves(){
        safeMoves.forEach(sq -> {
            sqcComps[sq.getKey()][sq.getValue()].setBackground(new java.awt.Color(204, 255, 153));});
    }

    /**
     * This method is called when we want to restore back the color of a square in our board if it has been highlighted.
     */
    public void deHighlightSafemoves(){
        safeMoves.forEach(sq -> {
            int row=sq.getKey();
            int col=sq.getValue();
            java.awt.Color colorSq=  board.getSquares()[row][col].getColor()==Color.WHITE  ? new java.awt.Color(255,253,208) : new java.awt.Color(196,8,16);
            sqcComps[row][col].setBackground(colorSq);});
    }
}

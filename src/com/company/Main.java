package com.company;


/**
 * initial main driver class, used to check the console outputs and test moves using a sample game
 */
public class Main {

    public static void main(String[] args) {

        //Sample main class to check functionality in console, used a
        // famous game between Paul Morphy and Duke Carl to test the moves

        Player p1 = new Player("Bob",1200,Color.WHITE);
        Player p2 = new Player("Pop",1300,Color.BLACK);
        Board board = new Board(p1,p2,8);


        //TODO : Paul Morphy vs Duke Karl (1858, Paris)
        p1.addMove('e', 2, 'e', 4);
        p2.addMove('e',7,'e', 5);


        p1.addMove('g',1,'f', 3);
        board.displayBoard();

        p2.addMove('d',7,'d', 6);
        board.displayBoard();

        p1.addMove('d',2,'d', 4);
        board.displayBoard();

        p2.addMove('c',8,'g', 4);
        board.displayBoard();

        p1.addMove('d',4,'e', 5);
        board.displayBoard();

        p2.addMove('g',4,'f', 3);
        board.displayBoard();

        p1.addMove('d',1,'f', 3);
        board.displayBoard();

        p2.addMove('d',6,'e', 5);
        board.displayBoard();

        p1.addMove('f',1,'c', 4);
        board.displayBoard();

        p2.addMove('g',8,'f', 6);
        board.displayBoard();

        p1.addMove('f',3, 'b', 3);
        board.displayBoard();

        p2.addMove('d',8,'e',7);
        board.displayBoard();

        p1.addMove('b',1,'c',3);
        board.displayBoard();

        p2.addMove('c',7,'c', 6);
        board.displayBoard();

        p1.addMove('c',1,'g', 5);
        board.displayBoard();

        p2.addMove('b',7,'b', 5);
        board.displayBoard();

        p1.addMove('c',3,'b', 5);
        board.displayBoard();

        p2.addMove('c',6,'b', 5);
        board.displayBoard();

        p1.addMove('c',4,'b', 5);
        board.displayBoard();

        p2.addMove('b',8,'d', 7);
        board.displayBoard();

        p1.addMove('e',1,'c', 1);
        board.displayBoard();

        //castling handled
        p2.addMove('a',8,'d', 8);
        board.displayBoard();

        p1.addMove('d',1,'d', 7);
        board.displayBoard();

        p2.addMove('d',8,'d', 7);
        board.displayBoard();

        p1.addMove('h',1,'d', 1);
        board.displayBoard();

        p2.addMove('e',7,'e', 6);
        board.displayBoard();

        p1.addMove('b',5,'d', 7);
        board.displayBoard();

        p2.addMove('f',6,'d', 7);
        board.displayBoard();

        p1.addMove('b',3,'b', 8);
        board.displayBoard();

        p2.addMove('d',7,'b', 8);
        board.displayBoard();

        p1.addMove('d',1,'d', 8);
        board.displayBoard();

        IPiece piece = p2.getBoard().getSquares()[7][3].getPiece();

        if (((PieceImpl) piece).isPlayerCheckmated(p2.getColor())){
            System.out.println("Checkmated");
        }
    }

    //static method to check the safe moves for each piece before starting (for testing/debugging)
    private static void printSafeMoves(Player p1, Board board) {
        for (int i =0; i<8;i++){
            for (int j=0; j<8; j++){
                IPiece piece = board.getSquares()[i][j].getPiece();
                if (piece!=null && piece.getColor().equals(p1.getColor())) {
                    System.out.println(piece.getPieceType());
                    piece.safeMoves().forEach(move->{
                        System.out.println(move.getKey() + " " + move.getValue());
                    });
                }

            }
        }
    }
}

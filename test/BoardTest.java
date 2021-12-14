import com.company.*;

import static org.junit.Assert.*;

import com.company.pieces.*;
import org.junit.Before;
import org.junit.Test;


public class BoardTest {
    Board tBoard;
    Player p1;
    Player p2;

    @Before
    public void setup() {
        p1 = new Player("Bob", 1200, Color.WHITE);
        p2 = new Player("Pop", 1210, Color.BLACK);
        tBoard = new Board(p1, p2,8);
        tBoard.displayBoard();
    }

    @Test
    public void testPawnsSetup() {

        for (int col = 0; col < tBoard.getSquares()[0].length; col++) {
            Pawn whitePawn = (Pawn) tBoard.getSquares()[1][col].getPiece();
            Pawn blackPawn = (Pawn) tBoard.getSquares()[6][col].getPiece();

            assertEquals(whitePawn.getPieceType(), Notation.P);
            assertEquals(whitePawn.getColor(),Color.WHITE);

            assertEquals(blackPawn.getPieceType(), Notation.P);
            assertEquals(blackPawn.getColor(),Color.BLACK);
        }
    }

    @Test
    public void testRookSetup() {
        Rook whiteQueenSideRook = (Rook) tBoard.getSquares()[0][0].getPiece();
        Rook whiteKingSideRook = (Rook) tBoard.getSquares()[0][7].getPiece();

        Rook blackQueenSideRook = (Rook) tBoard.getSquares()[7][0].getPiece();
        Rook blackKingSideRook = (Rook) tBoard.getSquares()[7][7].getPiece();

        assertEquals(whiteQueenSideRook.getPieceType(), Notation.R);
        assertEquals(whiteQueenSideRook.getColor(),Color.WHITE);

        assertEquals(whiteKingSideRook.getPieceType(), Notation.R);
        assertEquals(whiteKingSideRook.getColor(),Color.WHITE);

        assertEquals(blackQueenSideRook.getPieceType(), Notation.R);
        assertEquals(blackQueenSideRook.getColor(),Color.BLACK);

        assertEquals(blackKingSideRook.getPieceType(), Notation.R);
        assertEquals(blackKingSideRook.getColor(),Color.BLACK);
    }

    @Test
    public void testKnightSetup() {
        Knight whiteQueenSideKnight = (Knight) tBoard.getSquares()[0][1].getPiece();
        Knight whiteKingSideKnight = (Knight) tBoard.getSquares()[0][6].getPiece();

        Knight blackQueenSideKnight = (Knight) tBoard.getSquares()[7][1].getPiece();
        Knight blackKingSideKnight = (Knight) tBoard.getSquares()[7][6].getPiece();

        assertEquals(whiteQueenSideKnight.getPieceType(), Notation.N);
        assertEquals(whiteQueenSideKnight.getColor(),Color.WHITE);

        assertEquals(whiteKingSideKnight.getPieceType(), Notation.N);
        assertEquals(whiteKingSideKnight.getColor(),Color.WHITE);

        assertEquals(blackQueenSideKnight.getPieceType(), Notation.N);
        assertEquals(blackQueenSideKnight.getColor(),Color.BLACK);

        assertEquals(blackKingSideKnight.getPieceType(), Notation.N);
        assertEquals(blackKingSideKnight.getColor(),Color.BLACK);
    }

    @Test
    public void testBishopSetup() {
        Bishop whiteQueenSideBishop = (Bishop) tBoard.getSquares()[0][2].getPiece();
        Bishop whiteKingSideBishop = (Bishop) tBoard.getSquares()[0][5].getPiece();

        Bishop blackQueenSideBishop = (Bishop) tBoard.getSquares()[7][2].getPiece();
        Bishop blackKingSideBishop = (Bishop) tBoard.getSquares()[7][5].getPiece();

        assertEquals(whiteQueenSideBishop.getPieceType(), Notation.B);
        assertEquals(whiteQueenSideBishop.getColor(),Color.WHITE);

        assertEquals(whiteKingSideBishop.getPieceType(), Notation.B);
        assertEquals(whiteKingSideBishop.getColor(),Color.WHITE);

        assertEquals(blackQueenSideBishop.getPieceType(), Notation.B);
        assertEquals(blackQueenSideBishop.getColor(),Color.BLACK);

        assertEquals(blackKingSideBishop.getPieceType(), Notation.B);
        assertEquals(blackKingSideBishop.getColor(),Color.BLACK);
    }

    @Test
    public void testQueenSetup() {
        Queen whiteQueen = (Queen) tBoard.getSquares()[0][4].getPiece();
        Queen blackQueen = (Queen) tBoard.getSquares()[7][4].getPiece();

        assertEquals(whiteQueen.getPieceType(), Notation.Q);
        assertEquals(whiteQueen.getColor(),Color.WHITE);

        assertEquals(blackQueen.getPieceType(), Notation.Q);
        assertEquals(blackQueen.getColor(),Color.BLACK);
    }

    @Test
    public void testKingSetup() {
        King whiteKing = (King) tBoard.getSquares()[0][3].getPiece();
        King blackKing= (King) tBoard.getSquares()[7][3].getPiece();

        assertEquals(whiteKing.getPieceType(), Notation.K);
        assertEquals(whiteKing.getColor(),Color.WHITE);

        assertEquals(blackKing.getPieceType(), Notation.K);
        assertEquals(blackKing.getColor(),Color.BLACK);
    }

    @Test
    public void initialMovesKing(){
        King whiteKing = (King) tBoard.getSquares()[0][3].getPiece();
        King blackKing= (King) tBoard.getSquares()[7][3].getPiece();

        //initially no legal moves for king
        assertEquals(((PieceImpl) whiteKing).safeMoves().size(), 0);
        assertEquals(((PieceImpl) blackKing).safeMoves().size(), 0);

        p1.addMove('e',2,'e',4);
        p2.addMove('e',7,'e',5);

        //after pawn moves, one legal move for the king
        assertEquals(((PieceImpl) whiteKing).safeMoves().size(), 1);
        assertEquals(((PieceImpl) blackKing).safeMoves().size(), 1);

    }

    @Test
    public void initialMovesQueen(){
        Queen whiteQueen = (Queen) tBoard.getSquares()[0][4].getPiece();
        Queen blackQueen = (Queen) tBoard.getSquares()[7][4].getPiece();

        //initially no legal moves for queen
        assertEquals(0,((PieceImpl) whiteQueen).safeMoves().size());
        assertEquals(0,((PieceImpl) blackQueen).safeMoves().size());

        p1.addMove('d',2,'d',4);
        p2.addMove('d',7,'d',5);

        //after pawn moves, 2 legal moves for the queen
        assertEquals(2,((PieceImpl) whiteQueen).safeMoves().size());
        assertEquals(2,((PieceImpl) blackQueen).safeMoves().size());
    }


    @Test
    public void initialMovesKnight(){
        Knight whiteQueenSideKnight = (Knight) tBoard.getSquares()[0][1].getPiece();
        Knight whiteKingSideKnight = (Knight) tBoard.getSquares()[0][6].getPiece();

        Knight blackQueenSideKnight = (Knight) tBoard.getSquares()[7][1].getPiece();
        Knight blackKingSideKnight = (Knight) tBoard.getSquares()[7][6].getPiece();

        //initially 2 legal moves for the knight
        assertEquals(2,((PieceImpl) whiteQueenSideKnight).safeMoves().size());
        assertEquals(2,((PieceImpl) whiteKingSideKnight).safeMoves().size());
        assertEquals(2,((PieceImpl) blackQueenSideKnight).safeMoves().size());
        assertEquals(2,((PieceImpl) blackKingSideKnight).safeMoves().size());


        p1.addMove('g',1,'f',3);
        p2.addMove('b',8,'c',6);
        p1.addMove('b',1,'c',3);
        p2.addMove('g',8,'f',6);

        //after first knight, move, 5 moves are possible for each knight

        assertEquals(5,((PieceImpl) whiteQueenSideKnight).safeMoves().size());
        assertEquals(5,((PieceImpl) whiteKingSideKnight).safeMoves().size());
        assertEquals(5,((PieceImpl) blackQueenSideKnight).safeMoves().size());
        assertEquals(5,((PieceImpl) blackKingSideKnight).safeMoves().size());
    }


    @Test
    public void initialMovesBishop() {
        Bishop whiteQueenSideBishop = (Bishop) tBoard.getSquares()[0][2].getPiece();
        Bishop whiteKingSideBishop = (Bishop) tBoard.getSquares()[0][5].getPiece();

        Bishop blackQueenSideBishop = (Bishop) tBoard.getSquares()[7][2].getPiece();
        Bishop blackKingSideBishop = (Bishop) tBoard.getSquares()[7][5].getPiece();

        //initially no legal moves for bishop
        assertEquals(0,((PieceImpl) whiteQueenSideBishop).safeMoves().size());
        assertEquals(0,((PieceImpl) whiteKingSideBishop).safeMoves().size());
        assertEquals(0,((PieceImpl) blackQueenSideBishop).safeMoves().size());
        assertEquals(0,((PieceImpl) blackKingSideBishop).safeMoves().size());

        p1.addMove('e',2,'e',4);
        p2.addMove('e',7,'e',5);
        p1.addMove('d',2,'d',4);
        p2.addMove('d',7,'d',5);


        //after first 2 moves e4,d4 and its mirrors, 5 moves are possible for each bishop
        assertEquals(5,((PieceImpl) whiteQueenSideBishop).safeMoves().size());
        assertEquals(5,((PieceImpl) whiteKingSideBishop).safeMoves().size());
        assertEquals(5,((PieceImpl) blackQueenSideBishop).safeMoves().size());
        assertEquals(5,((PieceImpl) blackKingSideBishop).safeMoves().size());
    }


    @Test
    public void initialMovesRook() {
        Rook whiteQueenSideRook = (Rook) tBoard.getSquares()[0][0].getPiece();
        Rook whiteKingSideRook = (Rook) tBoard.getSquares()[0][7].getPiece();

        Rook blackQueenSideRook = (Rook) tBoard.getSquares()[7][0].getPiece();
        Rook blackKingSideRook = (Rook) tBoard.getSquares()[7][7].getPiece();

        //initially no legal moves for rook
        assertEquals(0,((PieceImpl) whiteQueenSideRook).safeMoves().size());
        assertEquals(0,((PieceImpl) whiteKingSideRook).safeMoves().size());
        assertEquals(0,((PieceImpl) blackQueenSideRook).safeMoves().size());
        assertEquals(0,((PieceImpl) blackKingSideRook).safeMoves().size());

        p1.addMove('h',2,'h',4);
        p2.addMove('h',7,'h',5);
        p1.addMove('a',2,'a',4);
        p2.addMove('a',7,'a',5);


        //after first 2 moves h4,a4 and its mirrors, 2 moves are possible for each rook
        assertEquals(2,((PieceImpl) whiteQueenSideRook).safeMoves().size());
        assertEquals(2,((PieceImpl) whiteKingSideRook).safeMoves().size());
        assertEquals(2,((PieceImpl) blackQueenSideRook).safeMoves().size());
        assertEquals(2,((PieceImpl) blackKingSideRook).safeMoves().size());
    }


    @Test
    public void initalMovesPawn() {
        //initially each pawn can move two squares
        for (int col = 0; col < tBoard.getSquares()[0].length; col++) {
            Pawn whitePawn = (Pawn) tBoard.getSquares()[1][col].getPiece();
            Pawn blackPawn = (Pawn) tBoard.getSquares()[6][col].getPiece();
            assertEquals(2,whitePawn.safeMoves().size());
            assertEquals(2,blackPawn.safeMoves().size());

        }
    }



}

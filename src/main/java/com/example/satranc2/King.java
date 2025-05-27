package com.example.satranc2;

public class King extends Piece {


    public King(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whiteKing.png" : "/images/blackKing.png");
    }




    @Override
    public boolean isValidMove(int targetRow, int targetCol) {

        if (targetRow == this.row && targetCol == this.col) { // king cant move his own square
            return false; }

        // calculate the move
        // math.abs() gives us a positive value ( distance )
        int dRow = Math.abs(targetRow - row);
        int dCol = Math.abs(targetCol - col);


        if (dRow <= 1 && dCol <= 1) { // normal king move is 1 square to all direction
            return true;
        }



        return false;
    }
}

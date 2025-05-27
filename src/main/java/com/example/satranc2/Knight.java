package com.example.satranc2;

public class Knight extends Piece {
    public Knight(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whiteKnight.png" : "/images/blackKnight.png");
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) return false;  // it cant move his own square
        int dRow = Math.abs(targetRow - row); // distance between target row and current row
        int dCol = Math.abs(targetCol - col);// distance between target col and curren col

        // knight move ( L shape )
        // it has to move 2 square on a row and 1 square on a col or 2 square on a col and 1 square on a row
        return (dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2);


    }
}

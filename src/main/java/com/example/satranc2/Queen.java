package com.example.satranc2;

public class Queen extends Piece {
    public Queen(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whiteQueen.png" : "/images/blackQueen.png");
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) return false;

        int dRow = Math.abs(targetRow - row);
        int dCol = Math.abs(targetCol - col);


        return (targetRow == row || targetCol == col) || (dRow == dCol); // straight and croos move
    }
}

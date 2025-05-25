package com.example.satranc2;

public class Knight extends Piece {
    public Knight(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whiteKnight.png" : "/images/blackKnight.png");
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) return false; // Yerinde durma geçersiz
        int dRow = Math.abs(targetRow - row);
        int dCol = Math.abs(targetCol - col);
        return (dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2);
    }
}

package com.example.satranc2;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whiteBishop.png" : "/images/blackBishop.png");
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) return false; // Yerinde durma engelleniyor
        return Math.abs(targetRow - row) == Math.abs(targetCol - col);
    }
}

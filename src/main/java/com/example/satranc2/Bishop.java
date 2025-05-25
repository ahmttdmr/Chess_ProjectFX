package com.example.satranc2;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whiteBishop.png" : "/images/blackBishop.png");
        // sets his inital position and color
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) return false; // cant move to the square its already on
        return Math.abs(targetRow - row) == Math.abs(targetCol - col); // bishops moves diagonally
    }
}

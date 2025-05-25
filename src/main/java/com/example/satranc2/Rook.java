package com.example.satranc2;

public class Rook extends Piece {


    public Rook(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whiteRook.png" : "/images/blackRook.png");
    }



    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) return false;

        return targetRow == row || targetCol == col; // straight move on current row or current col
    }
}

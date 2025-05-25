package com.example.satranc2;

public class Rook extends Piece {
    private boolean hasMoved = false;

    public Rook(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whiteRook.png" : "/images/blackRook.png");
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean moved) {
        this.hasMoved = moved;
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) return false; // Yerinde durma

        return targetRow == row || targetCol == col;
    }
}

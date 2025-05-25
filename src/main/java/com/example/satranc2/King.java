package com.example.satranc2;

public class King extends Piece {
    private boolean hasMoved = false;

    public King(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whiteKing.png" : "/images/blackKing.png");
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved() {
        this.hasMoved = true;
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {

        if (targetRow == this.row && targetCol == this.col) {
            return false; }


        int dRow = Math.abs(targetRow - row);
        int dCol = Math.abs(targetCol - col);

        // Normal şah hareketi
        if (dRow <= 1 && dCol <= 1) {
            return true;
        }

        // Rok (kısa ve uzun)
        if (!hasMoved && dRow == 0 && (dCol == 2)) {
            return true;
        }

        return false;
    }
}

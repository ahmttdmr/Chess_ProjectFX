package com.example.satranc2;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Pawn extends Piece {

    // Satranç tahtasına erişim için geçici static değişken
    public static GridPane boardRef;

    public Pawn(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whitePawn.png" : "/images/blackPawn.png");
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) return false;

        int direction = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;

        int dRow = targetRow - row;
        int dCol = targetCol - col;

        StackPane targetTile = getTile(targetRow, targetCol);
        boolean targetHasPiece = targetTile != null && targetTile.getChildren().stream()
                .anyMatch(node -> node instanceof Piece);

        // Düz ileri adım (hedef boş olmalı)
        if (dCol == 0) {
            if (dRow == direction && !targetHasPiece) return true;
            if (row == startRow && dRow == 2 * direction) {
                StackPane middleTile = getTile(row + direction, col);
                boolean pathClear = middleTile.getChildren().stream().noneMatch(n -> n instanceof Piece);
                return !targetHasPiece && pathClear;
            }
        }

        // Çapraz yeme — sadece karşı renk taş varsa
        if (Math.abs(dCol) == 1 && dRow == direction) {
            if (targetHasPiece) {
                for (Node node : targetTile.getChildren()) {
                    if (node instanceof Piece other && other.isWhite() != this.isWhite()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private StackPane getTile(int row, int col) {
        if (boardRef == null) return null;
        for (Node node : boardRef.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if (r != null && c != null && r == row && c == col) {
                return (StackPane) node;
            }
        }
        return null;
    }
}

package com.example.satranc2;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Pawn extends Piece {

    // it is a reference of board for all pawns
    // pawns can check other squares situation while moving
    public static GridPane boardRef;

    public Pawn(boolean isWhite, int row, int col) {
        super(isWhite, row, col, isWhite ? "/images/whitePawn.png" : "/images/blackPawn.png");
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol) {
        if (targetRow == row && targetCol == col) return false;

        int direction = isWhite ? -1 : 1; //  pawns move, if it is white up (-1), if it is black down (+1)
        int startRow = isWhite ? 6 : 1; // pawns start rows

        int dRow = targetRow - row;
        int dCol = targetCol - col;


        // is there a piece on target square ? lets find it
        StackPane targetTile = getTile(targetRow, targetCol); // find target square
        boolean targetHasPiece = targetTile != null && targetTile.getChildren().stream().anyMatch(node -> node instanceof Piece);
         // is there any piece on target square

        if (dCol == 0) { // if pawn wants to move straight
            if (dRow == direction && !targetHasPiece) return true;// if it moves on right direction and there is not another piece on target, return true
            if (row == startRow && dRow == 2 * direction) {// if pawn is on his beginning location and it moves 2 square on right direction
                StackPane middleTile = getTile(row + direction, col);// then middleTile should be empty pawn cant jump over it
                boolean pathClear = middleTile.getChildren().stream().noneMatch(n -> n instanceof Piece);
                // if there is not a enemy piece on target square and target square is empty
                return !targetHasPiece && pathClear; // it is a valid first move
            }
        }
        // if pawn wants to move cross ( for attack )
        if (Math.abs(dCol) == 1 && dRow == direction) {// if it moves 1 square to another col and moves straight direction
            if (targetHasPiece) { // it can be a attack move but is there any enemy piece on target square ?
                for (Node node : targetTile.getChildren()) { // look at the target square
                    if (node instanceof Piece other && other.isWhite() != this.isWhite()) {// is that piece has different color
                        // this is a valid attack move
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // that method helps pawn for finding a specific square on board
    // it works on board reference
    private StackPane getTile(int row, int col) {
        if (boardRef == null) return null; // if there is not a board reference do nothing
        for (Node node : boardRef.getChildren()) { // look every element on board
            Integer r = GridPane.getRowIndex(node); // take row of that element
            Integer c = GridPane.getColumnIndex(node);// take col of that element
            if (r != null && c != null && r == row && c == col) { // if that element is on the square that we wanted
                return (StackPane) node; // return that square
            }
        }
        return null;
    }
}

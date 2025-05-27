package com.example.satranc2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;


public abstract class Piece extends StackPane { // abstract means you cant create a piece directly, you create
                                                // specific pieces like bishop or pawn
    protected boolean isWhite;
    protected int row, col;
    protected ImageView imageView;

    public Piece(boolean isWhite, int row, int col, String imagePath) { // used to create a new piece
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;


        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView = new ImageView(image);
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);


        getChildren().add(imageView);   // add the image to the piece
        setupDragAndDropSource(); // set up the ability for this piece to be dragged and dropped
    }

    public boolean isWhite() { // returns true if the piece is white
        return isWhite;
    }

    public int getRow() { // gets the current row of the piece
        return row;
    }

    public int getCol() { // gets the current col of the piece
        return col;
    }

    public void setPosition(int row, int col) { // updates the piece's internal record of its position
        this.row = row;
        this.col = col;
    }

    public abstract boolean isValidMove(int targetRow, int targetCol);
    //'abstract' is so important; its says, as a piece class i dont know a move is valid but all classes
    //that inherited from me have to idenify their own validmove

    private void setupDragAndDropSource() {
        this.setOnDragDetected(event -> { //javafx.scene.Node

            if (getScene() == null) { // security control, if piece is not on the table dont do anything
                event.consume(); // stop the event
                return;
            }


            Object userData = getScene().getUserData(); // get any user data attached to the scene6,4,white
            // if the piece is on a tile (StackPane) and we found the main ChessApp game controller ...
            if (getParent() instanceof StackPane && userData instanceof ChessApp app) {
                // check if it's this piece's turn to move
                if (!app.isPieceTurn(this)) {
                    event.consume(); // not its turn so dont allow dragging
                    return;
                }
                // yes, its our turn. start the drag and drop
                Dragboard db = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                // put some simple info about the piece on the 'dragboard' ( like temporary note )
                content.putString(this.row + "," + this.col + "," + (this.isWhite ? "white" : "black"));
                db.setContent(content); // add this note to the drag
                // show a picture of the piece as it's being dragged
                db.setDragView(this.snapshot(null, null), event.getX(), event.getY());
                app.handleDragStarted(this); // tell the main ChessApp that we have started dragging this piece
                this.setVisible(false); // hide the original piece ( the dragged piecture will show )
                event.consume(); // drag started event handled
            } else {// if we couldn't get the ChessApp or piece isn't on a tile, cancel the drag
                event.consume();
            }
        });
         // this runs when the mouse button is released after dragging
        this.setOnDragDone(event -> {

            if (getScene() == null) { // security check if piece is not on screen
                event.consume(); // stop the event
                return;
            }

            Object userData = getScene().getUserData(); // get the ChessApp contoller again
//            if (userData instanceof ChessApp app) {
//                // tell the main ChessApp that this piece's drag is done,
//                // and whether it was successfully dropped on a target
//                app.handleDragDone(this, event.isDropCompleted());
//            }
            if (!event.isDropCompleted()) { // if the piece was not successfully dropped
                this.setVisible(true); // make the original piece show up again its old spot
            }
            event.consume(); // drag finished event handled
        });
    }
}
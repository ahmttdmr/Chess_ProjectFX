package com.example.satranc2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;


public abstract class Piece extends StackPane {
    protected boolean isWhite;
    protected int row, col;
    protected ImageView imageView;

    public Piece(boolean isWhite, int row, int col, String imagePath) {
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;

        // Resim yolu düzeltildi: getResourceAsStream ile çalışması için / ile başlamalı
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView = new ImageView(image);
        imageView.setFitWidth(80); // TILE_SIZE ile eşleşebilir veya sabit kalabilir
        imageView.setFitHeight(80); // TILE_SIZE ile eşleşebilir veya sabit kalabilir
        // imageView.setMouseTransparent(true); // Bu satır yorumlu kalmalı veya false olmalı ki StackPane sürüklemeyi algılasın

        getChildren().add(imageView);
        setupDragAndDropSource(); // Sürükle ve bırak kaynak olaylarını ayarla
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract boolean isValidMove(int targetRow, int targetCol);

    private void setupDragAndDropSource() {
        this.setOnDragDetected(event -> {
            // Scene null kontrolü ekleyelim
            if (getScene() == null) {
                event.consume();
                return;
            }

            // Tahtanın (Scene) userData'sından ChessApp örneğini al
            Object userData = getScene().getUserData();
            if (getParent() instanceof StackPane && userData instanceof ChessApp app) {
                if (!app.isPieceTurn(this)) {
                    event.consume();
                    return;
                }

                Dragboard db = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(this.row + "," + this.col + "," + (this.isWhite ? "white" : "black"));
                db.setContent(content);
                db.setDragView(this.snapshot(null, null), event.getX(), event.getY());
                app.handleDragStarted(this);
                this.setVisible(false);
                event.consume();
            } else {
                event.consume();
            }
        });

        this.setOnDragDone(event -> {
            // Scene null kontrolü ekleyelim
            if (getScene() == null) {
                event.consume();
                return;
            }

            Object userData = getScene().getUserData();
            if (userData instanceof ChessApp app) {
                app.handleDragDone(this, event.isDropCompleted());
            }
            if (!event.isDropCompleted()) {
                this.setVisible(true);
            }
            event.consume();
        });
    }
}
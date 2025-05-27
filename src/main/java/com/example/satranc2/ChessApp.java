package com.example.satranc2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.ChoiceDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class ChessApp extends Application {

    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private GridPane board;   // gridpane helps for the grid shape of chess board
    private Piece selectedPiece = null;  // it holds the selected piece for dragging
    private boolean whiteTurn = true; // it decides who plays first and turn

    private String player1;
    private String player2;

    private UserStatsManager statsManager;

    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;              // these booleans helps for castling move(Castling)
    private boolean whiteLeftRookMoved = false;
    private boolean whiteRightRookMoved = false;
    private boolean blackLeftRookMoved = false;
    private boolean blackRightRookMoved = false;

    @Override
    public void start(Stage primaryStage) {
        new LoginScreen().show(primaryStage, (p1, p2) -> {  // first shows login screen
            this.player1 = p1;
            this.player2 = p2;
            initChessBoard(primaryStage);   // then shows board
        });
    }

    private void initChessBoard(Stage primaryStage) {
        this.statsManager = new UserStatsManager();
        board = new GridPane();
        drawBoard();
        placePieces();

        Scene scene = new Scene(board, TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
        scene.setUserData(this); // store a reference to this main ChessApp instance in the scene,
                                // so other parts of the game ( like Pieces ) can access it
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Chess - " + player1 + " (White) vs " + player2 + " (Black)");
        primaryStage.setFullScreen(false);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Piece showPromotionDialog(boolean forWhite, int R, int C) {
        List<String> choices = new ArrayList<>();
        choices.add("Queen");
        choices.add("Rook");
        choices.add("Bishop");
        choices.add("Knight");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("Queen", choices); // it opens a little dialog box
        dialog.setTitle("Promotion");                                       // and sets queen as default
        dialog.setHeaderText("You can promote your pawn!");
        dialog.setContentText("Select a piece type to promote your pawn to:");

        if (board != null && board.getScene() != null && board.getScene().getWindow() != null) { // if UI is ready, set the main window as owner for the dialog
            dialog.initOwner((Stage) board.getScene().getWindow()); // it sets the main window as owner for the dialog because we want to show it over the main window
        }
        Optional<String> result = dialog.showAndWait(); // it blocks the UI thread until the user selects a piece type or closes the dialog
        String chosenPieceType = result.orElse("Queen"); // if player made a choice or closed the dialog, use the choice, else use default queen

        switch (chosenPieceType) {
            case "Queen": return new Queen(forWhite, R, C);
            case "Rook": return new Rook(forWhite, R, C);
            case "Bishop": return new Bishop(forWhite, R, C);
            case "Knight": return new Knight(forWhite, R, C);
            default: return new Queen(forWhite, R, C);
        }
    }


    public boolean isPieceTurn(Piece piece) {
        return piece.isWhite() == whiteTurn;
    } // it checks if the given piece is the same color as the current player.

    public void handleDragStarted(Piece piece) {

        selectedPiece = piece; // it sets the selected piece to the piece you dragged

        drawValidMoves(selectedPiece); // it draws the valid moves for the selected piece ( with green dots )
    }

    private boolean tryMoveSelectedPiece(int targetRow, int targetCol) {
        if (selectedPiece == null) return false; // if there is no selected piece there is no need to continue

        // castling move
        if (selectedPiece instanceof King king && Math.abs(targetCol - selectedPiece.getCol()) == 2 && targetRow == selectedPiece.getRow()) {
            // if selected piece is a King then lets call him king
            // is king want to go 2 squares ?
            // are king and selected square in same row?

            if (tryCastle(king, targetRow, targetCol)) { // we call a tryCastle method and if it returns true, it means that the castling move was successful
                // castling succeed
                whiteTurn = !whiteTurn;
                return true;
            } else {
                // castling failed
                return false;
            }
        }

        // Normal move control
        if (canMovePiece(selectedPiece, targetRow, targetCol)) { // we call a canMovePiece method and if it returns true, it means that the move is possible
            int fromRow = selectedPiece.getRow(); // selected piece's row'
            int fromCol = selectedPiece.getCol(); // selected piece's col'
            StackPane fromTile = getNodeFromGridPane(board, fromCol, fromRow); // selected piece's StackPane(square)
            StackPane toTile = getNodeFromGridPane(board, targetCol, targetRow); // target  StackPane(square)

            Piece capturedPiece = null; // we will check if there is a piece that can be captured in the target square but first lets initialize it with null value
                                         // now lets check target square
            for (Node nodeOnTargetTile : toTile.getChildren()) {
                if (nodeOnTargetTile instanceof Piece && nodeOnTargetTile != selectedPiece) { // if the target square is occupied by a piece and it's not the selected piece
                    capturedPiece = (Piece) nodeOnTargetTile; // we note it  as capturedPiece
                    break;
                }
            }

            // Simulating the move of the selected piece from the source square to the target square
            if (fromTile != null) fromTile.getChildren().remove(selectedPiece); // remove the selected piece from the source square
            if (capturedPiece != null) toTile.getChildren().remove(capturedPiece); // remove the captured piece from the target square
            selectedPiece.setPosition(targetRow, targetCol); // update the position of the selected piece
            toTile.getChildren().add(selectedPiece); // add the selected piece to the target square

            // Is player threads his own King ?  // we simulated previous step because we need to check this situation
            if (isKingInCheck(selectedPiece.isWhite())) { // if it turns true value then the move is invalid
                toTile.getChildren().remove(selectedPiece); // remove the selected piece from the target square
                selectedPiece.setPosition(fromRow, fromCol); // update the position of the selected piece
                if (fromTile != null) fromTile.getChildren().add(selectedPiece); // add the selected piece to the source square
                if (capturedPiece != null) toTile.getChildren().add(capturedPiece); // add the captured piece to the target square
                System.out.println("Invalid move!:King will be in check!");

                return false;
            }

            // Move is valid, we can use the simulated move as valid move
            updateMoveFlags(selectedPiece);// if king or rook moved, update the flags for castling move for future
            Piece pieceThatMoved = selectedPiece; // we noted the selected piece as pieceThatMoved for special cases like pawn promotion

            if (pieceThatMoved instanceof Pawn pawn) { // if the selected piece is a pawn
                int promotionRank = pawn.isWhite() ? 0 : 7; // for white pawns, it's 0, for black pawns, it's 7 to promotion row
                if (pawn.getRow() == promotionRank) { // if the selected piece is a pawn and it's on the promotion row
                    board.setDisable(true); // player should choose a piece to promote and can't touch the board
                    Piece promotedPiece = showPromotionDialog(pawn.isWhite(), pawn.getRow(), pawn.getCol()); // show the promotion dialog
                    board.setDisable(false); // player chose a piece to promote now he can touch the board again
                    toTile.getChildren().remove(pawn); // remove the old pawn
                    toTile.getChildren().add(promotedPiece); // add the promoted piece to the target square

                }
            }

            whiteTurn = !whiteTurn; // pass the turn to the other player
            checkGameStateAfterMove(); // after the move,check 'check' situation

            return true;
        }

        return false;
    }

    private boolean canMovePiece(Piece piece, int targetRow, int targetCol) { // controls valid move
        if (!piece.isValidMove(targetRow, targetCol)) { // piece.isValidMove method looks piece's special move rules
            return false;
        }
        if (!isPathClear(piece, targetRow, targetCol)) { // isPathClear method looks the road between source and target squares clear or not
            return false;                                // knight is not a part of this because knight can jump over other pieces
        }

        StackPane targetTileNode = getNodeFromGridPane(board, targetCol, targetRow); // find the target square StackPane
        if (targetTileNode != null) { // if the target square is existing
            for (Node node : targetTileNode.getChildren()) { // look inside the target square
                if (node instanceof Piece && node != piece) { // if the target square is occupied by a piece and it's not the selected piece
                    Piece occupyingPiece = (Piece) node; // lets call that peice as occupyingPiece //(Piece) casting the node to Piece type
                    if (occupyingPiece.isWhite() == piece.isWhite()) { // is the peice occupying square white or black?
                        return false; // if the occupying piece is the same color with the selected piece, it's an invalid move
                    }
                }
            }
        }
        return true;
    }

    private void checkGameStateAfterMove() { // this method checks game situation after moves
        if (isKingInCheck(whiteTurn)) {
            String currentPlayerName = whiteTurn ? player1 : player2; // find the player in check
            String currentPlayerColor = whiteTurn ? "White" : "Black"; // find the player's color
            System.out.println(currentPlayerName + " (" + currentPlayerColor + ") is in check!");

            if (isCheckmate(whiteTurn)) { // if there is checkmate situation
                System.out.println("Checkmate!");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game is over!");
                alert.setHeaderText("Checkmate!");

                String winnerName, loserName, winnerColorDisplay, loserColorDisplay;
                if (whiteTurn) { // if white player is in checkmate situation then Black wins
                    winnerName = player2; loserName = player1;
                    winnerColorDisplay = "Black"; loserColorDisplay = "White";
                } else { // if the black player is in checkmate situation then White wins
                    winnerName = player1; loserName = player2;
                    winnerColorDisplay = "White"; loserColorDisplay = "Black";
                }

                statsManager.incrementWin(winnerName); // increase the win count of the winner


                alert.setContentText(winnerName + " (" + winnerColorDisplay + ") won! " + loserName + " (" + loserColorDisplay + ") checkmated.");

                if (board != null && board.getScene() != null && board.getScene().getWindow() != null) { // we did the same thing before
                    alert.initOwner((Stage) board.getScene().getWindow());
                }
                alert.showAndWait();
                board.setDisable(true);
            }
        }
    }

    // this method checks if the path between source and target squares is clear or not for castling
    private boolean isSquareEmptyForCastle(int row, int col) {
        StackPane tile = getNodeFromGridPane(board, col, row); // find the target square StackPane
        if (tile == null) return false; // it is a security, if the target square is not existing then return false
        for (Node node : tile.getChildren()) { // look inside the target square
            if (node instanceof Piece) { // if the target square is occupied by a piece
                return false;
            }
        }
        return true; // if the target square is empty then return true
    }

    // is square is attacked by any piece?
    private boolean isSquareAttacked(int row, int col, boolean byWhite) {
        // if byWhite is true, then attacked squares are white pieces
        // if byWhite is false, then attacked squares are black pieces

        for (Node tileNodeOuter : board.getChildren()) { // looks every square of the board
            if (tileNodeOuter instanceof StackPane) { // if the square is a StackPane
                StackPane currentSquare = (StackPane) tileNodeOuter; // take that square as current square
                for (Node pieceNode : currentSquare.getChildren()) { // look inside the current square
                    if (pieceNode instanceof Piece) {  // if the current square is occupied by a piece
                        Piece piece = (Piece) pieceNode; // lets call that piece as piece
                        if (piece.isWhite() == byWhite) { // is that piece has same color with the attacked square?

                            if (piece.isValidMove(row, col) && isPathClear(piece, row, col)) {
                                // is it possible for this piece to move to the target square according to its own movement rules?
                                // is the road between source and target squares clear?
                                return true;// target square is attacked by a piece
                            }
                        }
                    }
                }
            }
        }
        return false; // square is not attacked by any piece
    }

    private boolean tryCastle(King king, int row, int col) {


        boolean isWhiteCastling = king.isWhite(); // is king white or black?
        int expectedKingColStart = 4; // for castling king should start from 4th col

        if (king.getCol() != expectedKingColStart) return false; // if the king's col is not 4th col, it's invalid move
        if ((isWhiteCastling && whiteKingMoved) || (!isWhiteCastling && blackKingMoved)) return false;
        // kings must not move before castling move

         // short castling
        if (col == 6) {
            if ((isWhiteCastling && whiteRightRookMoved) || (!isWhiteCastling && blackRightRookMoved)) return false;
            // check for past rook move ( rooks must not move before castling move )

            if (!isSquareEmptyForCastle(row, 5) || !isSquareEmptyForCastle(row, 6)) return false;
            // between the king and rook must be empty

            if (isSquareAttacked(row, 4, !isWhiteCastling) ||
                    isSquareAttacked(row, 5, !isWhiteCastling) ||
                    isSquareAttacked(row, 6, !isWhiteCastling)) return false;
            // is king's current position safe ?
            // is the square that king will jump over is attacked?
            // is the square that king will move to is attacked?
            // !isWhiteCastling means is the attacking side the opponent ?




            movePieceForCastle(row, 7, row, 5); //  Rook   H1/H8 to F1/F8
            movePieceForCastle(row, 4, row, 6); //  King   E1/E8 to G1/G8



            if(isWhiteCastling) { whiteKingMoved = true; whiteRightRookMoved = true;}// if White castling happens, update the flags
            else { blackKingMoved = true; blackRightRookMoved = true;} // if Black castling happens, update the flags
            return true;

         // long castling
        } else if (col == 2) {
            if ((isWhiteCastling && whiteLeftRookMoved) || (!isWhiteCastling && blackLeftRookMoved)) return false;

            if (!isSquareEmptyForCastle(row, 1) || !isSquareEmptyForCastle(row, 2) || !isSquareEmptyForCastle(row, 3)) return false;

            if (isSquareAttacked(row, 4, !isWhiteCastling) ||
                    isSquareAttacked(row, 3, !isWhiteCastling) ||
                    isSquareAttacked(row, 2, !isWhiteCastling)) return false;

            movePieceForCastle(row, 0, row, 3);
            movePieceForCastle(row, 4, row, 2);

            if(isWhiteCastling) { whiteKingMoved = true; whiteLeftRookMoved = true;}
            else { blackKingMoved = true; blackLeftRookMoved = true;}
            return true;
        }
        return false; // if king doesn't want to move to 6. or 2. col, it's not castling move
    }

    private void movePieceForCastle(int fromRow, int fromCol, int toRow, int toCol) {
        StackPane fromTile = getNodeFromGridPane(board, fromCol, fromRow); // find the source square StackPane
        StackPane toTile = getNodeFromGridPane(board, toCol, toRow); // find the target square StackPane
        if (fromTile == null || toTile == null) return; // if there is not a source or target square( it is impossible but we can check ), return immediately

        Piece movingPiece = null; // reserve a empty box for moving piece
        for (Node node : fromTile.getChildren()) { // look inside the source square
            if (node instanceof Piece) { // if the source square is occupied by a piece
                movingPiece = (Piece) node; // that's the piece we want to move, we will put it inside movingPiece box
                break;
            }
        }
        if (movingPiece == null) return; // if there is no piece in the source square, return immediately

        fromTile.getChildren().remove(movingPiece); // remove the piece from the source square

        movingPiece.setPosition(toRow, toCol); // change the location of the piece to the target square
        toTile.getChildren().add(movingPiece);// put the piece to the target square


    }

    private void updateMoveFlags(Piece piece) {// it is for castling move
        if (piece instanceof King) { // if the moving piece is a king
            if (piece.isWhite()) whiteKingMoved = true; // if this king is white, update the flags
            else blackKingMoved = true;// if this king is black, update the flags

        } else if (piece instanceof Rook) { // if the moving piece is a rook

            if (piece.isWhite()) {
                if (piece.getCol() == 0 && piece.getRow() == 7) whiteLeftRookMoved = true; // a1 rook
                else if (piece.getCol() == 7 && piece.getRow() == 7) whiteRightRookMoved = true; // h1 rook
            } else {
                if (piece.getCol() == 0 && piece.getRow() == 0) blackLeftRookMoved = true; // a8 rook
                else if (piece.getCol() == 7 && piece.getRow() == 0) blackRightRookMoved = true; // h8 rook
            }
        }
    }

    private King findKing(boolean white) { // when white is true it will find white king else black king
        for (Node node : board.getChildren()) { // looks every square of the board
            if (node instanceof StackPane) { // if the square is a StackPane
                StackPane tile = (StackPane) node; // take that square as tile
                for (Node child : tile.getChildren()) { // look inside the tile
                    if (child instanceof King && ((King) child).isWhite() == white) { // if the tile is a king and the king is white or black
                        return (King) child; // return the found king
                    }
                }
            }
        }
        return null; // it should never come here, but if it does, return null
    }

    private boolean isKingInCheck(boolean whiteKing) {
        King king = findKing(whiteKing);
        if (king == null) return false;

        int kingRow = king.getRow();
        int kingCol = king.getCol();


        return isSquareAttacked(kingRow, kingCol, !whiteKing);
    }

    private boolean isCheckmate(boolean forWhoseKing) {
        if (!isKingInCheck(forWhoseKing)) {          // if king is not in check then it is not checkmate situation
            return false;
        }


        for (Node nodeFromBoard : board.getChildren()) { // looks every square of the board
            if (!(nodeFromBoard instanceof StackPane currentTileOfPiece)) { // if the square is not a StackPane, continue
                continue;
            }

            Piece pieceToTest = null; // reserve a empty box for testing piece
            for (Node nodeInTile : currentTileOfPiece.getChildren()) { // look inside the square
                if (nodeInTile instanceof Piece p) { // if the square is occupied by a piece call it p
                    pieceToTest = p;
                    break;
                }
            }

            if (pieceToTest == null || pieceToTest.isWhite() != forWhoseKing) { // if there is no piece on this square OR the piece is not your color
                                                                                 // (i.e. not the piece of the player whose king is under threat), skip this piece
                continue;
            }

            // this is a piece of 'forWhoseKing' we will try all possible moves for this piece
            int originalRow = pieceToTest.getRow(); // don't forget the original position of the piece
            int originalCol = pieceToTest.getCol();

            for (int r_target = 0; r_target < BOARD_SIZE; r_target++) { // try all rows of the board
                for (int c_target = 0; c_target < BOARD_SIZE; c_target++) { // try all columns of the board
                                                                     // we trying to move the piece to all squares of the board

                    if (!pieceToTest.isValidMove(r_target, c_target)) continue; // is basic rules of move valid ?

                    if (!(pieceToTest instanceof Knight) && !isPathClear(pieceToTest, r_target, c_target)) continue;
                   // if the piece is not knight and the path between source and target squares is not clear, skip this move

                    // is there our piece in the target square ?
                    StackPane targetTileForSim = getNodeFromGridPane(board, c_target, r_target); //find target square StackPane
                    Piece pieceOnTargetSquare = null;
                    if (targetTileForSim != null) {
                        for (Node nodeOnTarget : targetTileForSim.getChildren()) {
                            if (nodeOnTarget instanceof Piece p) {
                                pieceOnTargetSquare = p;
                                break;
                            }
                        }
                    }
                    if (pieceOnTargetSquare != null && pieceOnTargetSquare.isWhite() == pieceToTest.isWhite()) {
                        continue; // if there is our piece in the target square, skip this move
                    }

                    // simulate the move
                    pieceToTest.setPosition(r_target, c_target); // change the position of the piece imaginary
                    currentTileOfPiece.getChildren().remove(pieceToTest); // remove the piece imaginary from the board
                    if (pieceOnTargetSquare != null) targetTileForSim.getChildren().remove(pieceOnTargetSquare); // remove the target square imaginary
                    targetTileForSim.getChildren().add(pieceToTest); // add the piece to the target square imaginary

                    boolean stillInCheckAfterSimulatedMove = isKingInCheck(forWhoseKing); // is king still in check

                    // take back the simulation
                    targetTileForSim.getChildren().remove(pieceToTest);
                    if (pieceOnTargetSquare != null) targetTileForSim.getChildren().add(pieceOnTargetSquare);
                    currentTileOfPiece.getChildren().add(pieceToTest);
                    pieceToTest.setPosition(originalRow, originalCol); // bring piece place back

                    if (!stillInCheckAfterSimulatedMove) {
                        return false; // after the simulation if king is safe we found a savior move it is not checkmate
                    }
                }
            }
        }
        return true; // its checkmate
    }

    private boolean isPathClear(Piece piece, int targetRow, int targetCol) {
        if (piece instanceof Knight) {
            return true; // knight dont need check for path
        }

        int fromRow = piece.getRow(); // piece's current row
        int fromCol = piece.getCol(); // piece's current col

        int dRow = targetRow - fromRow; // how many row piece will go
        int dCol = targetCol - fromCol; // how many col piece will go

        int steps = Math.max(Math.abs(dRow), Math.abs(dCol)); // how many steps it will take
        if (steps <= 1) return true; // if piece can go to target square with one step, it means path is clear

        int rowStep = Integer.compare(dRow, 0); // which direction piece will go on row  (+1:down,-1:up,0:stay)
        int colStep = Integer.compare(dCol, 0); // which direction piece will go on col

        // situation of pawn"s first move ( pawns can move 2 square at the beginning )
        if (piece instanceof Pawn && Math.abs(dRow) == 2 && dCol == 0) {
            // if the moving piece is a pawn and it moves 2 square and it doesn't change its col
            // then it is pawn's first 2 square move

            StackPane middleTile = getNodeFromGridPane(board, fromCol, fromRow + rowStep);
            // while pawn jumping over 2 squares, there is a square that pawn jumps over
            // that square is middleTile
            //fromRow + rowStep gives us middleTile's row (col stays same)

            if (middleTile != null) { // if middleTile is exist
                for(Node nodeInMiddle : middleTile.getChildren()){ // look inside the middleTile
                    if(nodeInMiddle instanceof Piece) return false; // if there is a piece inside middleTile, pawn cant jump over it
                }
            }

            return true; // middleTile is empty, pawn can move
        }


        // for other pieces ( rook,bishop,queen )
        for (int i = 1; i < steps; i++) {
            // this for loop checks every square between current square and target square

            int currentRow = fromRow + i * rowStep; // square's row number at that moment
            int currentCol = fromCol + i * colStep; // square's col number at that moment
            StackPane tile = getNodeFromGridPane(board, currentCol, currentRow); // find that middle squares
            if (tile != null) { // if that square is exist
                for (Node node : tile.getChildren()) { // look inside that square
                    if (node instanceof Piece) { // if there is a piece inside of that
                        return false; // path is closed
                    }
                }
            } else { // there is not a valid square on the road
                return false; // then path is closed
            }
        }
        return true; // path is open
    }

    private void drawBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle square = new Rectangle(TILE_SIZE, TILE_SIZE);
                square.setFill((row + col) % 2 == 0 ? Color.rgb(211, 211, 211) : Color.rgb(169, 169, 169));

                StackPane tile = new StackPane(); // create a box
                tile.getChildren().add(square); // put that squares inside the box

                final int currentRow = row; // note that square's row number
                final int currentCol = col; // note that square's col number


                // drag and drop situations
                tile.setOnDragOver(event -> { // when something dragged on this square
                    if (event.getGestureSource() != tile && event.getDragboard().hasString()) {
                        // if the dragged thing is not a square and the dragged thing is a piece( it has string )
                        if (selectedPiece != null) { // and if there is a selected piece
                            event.acceptTransferModes(TransferMode.MOVE); // then we can drag a piece to this square
                        }
                    }
                    event.consume(); // we have handled this event there, so stop it from going further
                });

                tile.setOnDragDropped(event -> { // something dropped on this square
                    Dragboard db = event.getDragboard();// take the data that cames with dragging
                    boolean moveSuccessful = false;// is move successful ? at he beginning it is not
                    if (db.hasString() && selectedPiece != null) {
                        // if the dropped data is a valid data and there is a selected piece
                        int targetR = currentRow; // the row of this dropped square
                        int targetC = currentCol; // the col of this dropped square

                        if (tryMoveSelectedPiece(targetR, targetC)) { // we need to ask that method can we take that selected piece to target square ?
                            // if it turns true
                            moveSuccessful = true; // move is succesful
                            selectedPiece.setVisible(true); // make that piece visible at new location ( while the dragging it was invisible )

                        }
                    }
                    event.setDropCompleted(moveSuccessful); // report the result of the drag operation
                    clearValidMoves(); // clear the valid moves
                    selectedPiece = null; //
                    event.consume();
                });


                board.add(tile, col, row); // add that square on board
            }
        }
    }

    private void placePieces() {
        Pawn.boardRef = board;// it gives a refference of board to pawn class, so pawn class can check the board's situation
        // for special moves like en passant
        // Pawns
        for (int col = 0; col < BOARD_SIZE; col++) {
            addPiece(new Pawn(false, 1, col)); // black Pawns
            addPiece(new Pawn(true, 6, col));  // white Pawns
        }

        // Rooks
        addPiece(new Rook(false, 0, 0)); addPiece(new Rook(false, 0, 7)); // black rooks
        addPiece(new Rook(true, 7, 0)); addPiece(new Rook(true, 7, 7));   // white rooks

        // Knights
        addPiece(new Knight(false, 0, 1)); addPiece(new Knight(false, 0, 6)); // black knights
        addPiece(new Knight(true, 7, 1)); addPiece(new Knight(true, 7, 6));   // white knights

        // Bishops
        addPiece(new Bishop(false, 0, 2)); addPiece(new Bishop(false, 0, 5)); // black bishops
        addPiece(new Bishop(true, 7, 2)); addPiece(new Bishop(true, 7, 5));   // white bishops

        // Queens
        addPiece(new Queen(false, 0, 3)); // black queen
        addPiece(new Queen(true, 7, 3));  // white queen

        // Kings
        addPiece(new King(false, 0, 4)); // black king
        addPiece(new King(true, 7, 4));  // white king
    }

    private void addPiece(Piece piece) {
        StackPane tile = getNodeFromGridPane(board, piece.getCol(), piece.getRow());
        // find the target tile on the board based on the piece's coordinates

        if (tile != null) { // only add the piece if a valid square was found
            tile.getChildren().add(piece); // this makes the piece appear on top of the square
        }
    }

    private StackPane getNodeFromGridPane(GridPane grid, int col, int row) { // this method finds the square that we wanted from gridpane

        for (Node node : grid.getChildrenUnmodifiable()) { // look at each square in the board
            Integer nodeCol = GridPane.getColumnIndex(node); // get the row and col of the current square
            Integer nodeRow = GridPane.getRowIndex(node);


            if (nodeCol != null && nodeRow != null) { // if this square has a location and it matches what we want
                if (nodeCol == col && nodeRow == row) {
                    return (StackPane) node; // return that square
                }
            }
        }
        return null;
    }

    private void drawValidMoves(Piece piece) {
        clearValidMoves(); // clear present move path
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {      // look at the every square on the board

                if (canMovePiece(piece, r, c)) { // can piece go to that square ?
                    StackPane tile = getNodeFromGridPane(board, c, r); // if it turns true, find that square from board
                    if (tile != null) { // if we found that square
                        Circle dot = new Circle(TILE_SIZE / 8.0, Color.rgb(0, 255, 0, 0.5));
                        dot.setMouseTransparent(true); // this way, when we click on the green dot, we actually click on the square below it
                        tile.getChildren().add(dot);  // put green dots on valid move squares
                    }
                }
            }
        }
    }

    private void clearValidMoves() {        // clear green dots
        for (Node tileNode : board.getChildrenUnmodifiable()) { // go through each square on the board
            if (tileNode instanceof StackPane) { // check if its is a actually a square
                StackPane tile = (StackPane) tileNode; // get the square
                tile.getChildren().removeIf(node -> node instanceof Circle); //from this square remove anything that is a circle ( our green dots )
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
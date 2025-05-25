package com.example.satranc2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Handles the player login/register screen.
public class LoginScreen {

    // Interface to tell the main game when login is done.
    public interface LoginCallback {
        // Called when login is successful, passes player names.
        void onLoginSuccess(String player1, String player2);
    }

    // Manages user accounts (checks passwords, registers new users).
    private final UserManager userManager = new UserManager();

    // Sets up and shows the login window.
    public void show(Stage stage, LoginCallback callback) {
        // Input field for Player 1's name.
        TextField player1Field = new TextField();
        player1Field.setPromptText("White Player Name"); // Placeholder text.

        // Input field for Player 1's password.
        PasswordField player1Pass = new PasswordField();
        player1Pass.setPromptText("White Player Password"); // Placeholder text.

        // Input field for Player 2's name.
        TextField player2Field = new TextField();
        player2Field.setPromptText("Black Player Name"); // Placeholder text.

        // Input field for Player 2's password.
        PasswordField player2Pass = new PasswordField();
        player2Pass.setPromptText("Black Player Password"); // Placeholder text.

        Button loginButton = new Button("Login"); // Login button.
        Button registerButton = new Button("Register new account"); // Register button.
        Label statusLabel = new Label(); // Shows messages like "Login failed".

        // Button to skip login (for quick testing).
        Button BypassButton = new Button("Bypass");
        BypassButton.setOnAction(e -> {
            // If bypassed, use default names and start game.
            callback.onLoginSuccess("Side", "Side");
        });

        // When the Login button is clicked...
        loginButton.setOnAction(e -> {
            String p1 = player1Field.getText().trim(); // Get Player 1 name.
            String p2 = player2Field.getText().trim(); // Get Player 2 name.
            String pw1 = player1Pass.getText().trim(); // Get Player 1 password.
            String pw2 = player2Pass.getText().trim(); // Get Player 2 password.

            // Check if both players' details are correct.
            if (userManager.authenticate(p1, pw1) && userManager.authenticate(p2, pw2)) {
                // If yes, tell the main game and pass player names.
                callback.onLoginSuccess(p1, p2);
            } else {
                // If no, show an error message.
                statusLabel.setText("Login Failed! Check your username and password.");
            }
        });

        // When the Register button is clicked...
        registerButton.setOnAction(e -> {
            String p1 = player1Field.getText().trim(); // Get Player 1 name.
            String p2 = player2Field.getText().trim(); // Get Player 2 name.
            String pw1 = player1Pass.getText().trim(); // Get Player 1 password.
            String pw2 = player2Pass.getText().trim(); // Get Player 2 password.

            // Make sure all fields are filled.
            if (p1.isEmpty() || pw1.isEmpty() || p2.isEmpty() || pw2.isEmpty()) {
                statusLabel.setText("All Fields are required! Please fill them in.");
                return; // Stop if any field is empty.
            }

            // Try to register both users.
            boolean reg1 = userManager.registerUser(p1, pw1);
            boolean reg2 = userManager.registerUser(p2, pw2);

            // Show message based on registration result.
            if (reg1 && reg2) { // If both registered successfully.
                statusLabel.setText("Registeration successful! You can now login with your new account.");
            } else { // If registration failed (e.g., username taken).
                statusLabel.setText("Registration failed: Username already exists.");
            }
        });

        // Arrange all UI elements vertically in a VBox.
        // 10 is the spacing between elements.
        VBox vbox = new VBox(10,
                new Label("Chess Login / Register"), // Title label.
                player1Field, player1Pass,          // P1 inputs.
                player2Field, player2Pass,          // P2 inputs.
                loginButton, registerButton,        // Action buttons.
                statusLabel, BypassButton);         // Status message and bypass.
        vbox.setAlignment(Pos.CENTER); // Center everything in the VBox.
        vbox.setPrefSize(400, 400);    // Set preferred size for the VBox.

        // Put the VBox layout into a Scene.
        stage.setScene(new Scene(vbox));
        stage.setTitle("Login / Register"); // Set the window title.
        stage.show(); // Show the login window to the user.
    }
}
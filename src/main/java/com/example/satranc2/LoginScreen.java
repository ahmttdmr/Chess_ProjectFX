package com.example.satranc2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    public interface LoginCallback {
        void onLoginSuccess(String player1, String player2);
    }

    private final UserManager userManager = new UserManager();

    public void show(Stage stage, LoginCallback callback) {
        TextField player1Field = new TextField();
        player1Field.setPromptText("Beyaz Oyuncu Adı");

        PasswordField player1Pass = new PasswordField();
        player1Pass.setPromptText("Beyaz Şifre");

        TextField player2Field = new TextField();
        player2Field.setPromptText("Siyah Oyuncu Adı");

        PasswordField player2Pass = new PasswordField();
        player2Pass.setPromptText("Siyah Şifre");

        Button loginButton = new Button("Giriş Yap");
        Button registerButton = new Button("Kayıt Ol");
        Label statusLabel = new Label();

        Button BypassButton = new Button("Bypass");
        BypassButton.setOnAction(e -> {callback.onLoginSuccess("Kenar", "Kenar");});

        loginButton.setOnAction(e -> {
            String p1 = player1Field.getText().trim();
            String p2 = player2Field.getText().trim();
            String pw1 = player1Pass.getText().trim();
            String pw2 = player2Pass.getText().trim();

            if (userManager.authenticate(p1, pw1) && userManager.authenticate(p2, pw2)) {
                callback.onLoginSuccess(p1, p2);
            } else {
                statusLabel.setText("Giriş başarısız: Kullanıcı adı veya şifre hatalı.");
            }
        });

        registerButton.setOnAction(e -> {
            String p1 = player1Field.getText().trim();
            String p2 = player2Field.getText().trim();
            String pw1 = player1Pass.getText().trim();
            String pw2 = player2Pass.getText().trim();

            if (p1.isEmpty() || pw1.isEmpty() || p2.isEmpty() || pw2.isEmpty()) {
                statusLabel.setText("Tüm alanlar doldurulmalı.");
                return;
            }

            boolean reg1 = userManager.registerUser(p1, pw1);
            boolean reg2 = userManager.registerUser(p2, pw2);

            if (reg1 && reg2) {
                statusLabel.setText("Kayıt başarılı! Giriş yapabilirsiniz.");
            } else {
                statusLabel.setText("Kayıt başarısız: Kullanıcı adı zaten mevcut.");
            }
        });

        VBox vbox = new VBox(10,
                new Label("Satranç Girişi / Kayıt"),
                player1Field, player1Pass,
                player2Field, player2Pass,
                loginButton, registerButton,
                statusLabel, BypassButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(400, 400);

        stage.setScene(new Scene(vbox));
        stage.setTitle("Giriş / Kayıt");
        stage.show();
    }
}

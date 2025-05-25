package com.example.satranc2;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final String FILE_NAME = "users.txt";
    private Map<String, String> users = new HashMap<>();

    public UserManager() {
        loadUsers();
    }

    private void loadUsers() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2); // Split into 2 parts max
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // kullanıcı zaten var
        }

        users.put(username, password); // Store plain password

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(username + ":" + password); // Write plain password
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean authenticate(String username, String password) {
        String storedPassword = users.get(username);
        // Compare plain passwords directly
        return storedPassword != null && storedPassword.equals(password);
    }
}
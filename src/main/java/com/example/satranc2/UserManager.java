package com.example.satranc2;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Manages user accounts (usernames and passwords).
public class UserManager {
    private static final String FILE_NAME = "users.txt"; // File to store user data.
    private Map<String, String> users = new HashMap<>(); // Stores username -> password.

    // When UserManager is created, load users from file.
    public UserManager() {
        loadUsers();
    }

    // Loads users from the "users.txt" file.
    private void loadUsers() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return; // If file doesn't exist, nothing to load.

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Read each line from the file.
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2); // Split line by ":" into username and password.
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]); // Add user to our 'users' map.
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error if something goes wrong reading the file.
        }
    }

    // Registers a new user.
    public boolean registerUser(String username, String password) {
        // Check if username already exists.
        if (users.containsKey(username)) {
            return false; // Username taken, can't register.
        }

        users.put(username, password); // Add new user to the 'users' map.

        // Save the new user to the "users.txt" file.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) { // 'true' means append to file.
            writer.write(username + ":" + password); // Write username:password.
            writer.newLine(); // Add a new line for the next user.
        } catch (IOException e) {
            e.printStackTrace(); // Print error if something goes wrong writing to file.
        }

        return true; // Registration successful.
    }

    // Checks if the given username and password are correct.
    public boolean authenticate(String username, String password) {
        String storedPassword = users.get(username); // Get the stored password for this username.
        // Check if a password was stored and if it matches the given password.
        return storedPassword != null && storedPassword.equals(password);
    }
}
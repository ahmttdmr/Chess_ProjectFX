package com.example.satranc2;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Manages player win statistics.
public class UserStatsManager {

    private static final String STATS_FILE_NAME = "user_stats.txt"; // File to store win stats.

    // Stores username -> number of wins.
    private final Map<String, Integer> userWins = new HashMap<>();

    // When UserStatsManager is created, load stats from file.
    public UserStatsManager() {
        loadStats();
    }

    // Loads win statistics from the "user_stats.txt" file.
    private void loadStats() {
        File file = new File(STATS_FILE_NAME);
        if (!file.exists()) { // If file doesn't exist, nothing to load.
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Read each line from the file.
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":"); // Split line by ":" into username and wins.
                if (parts.length == 2) {
                    String username = parts[0];
                    try {
                        int wins = Integer.parseInt(parts[1]); // Convert win count string to number.
                        userWins.put(username, wins);          // Add user's win count to our map.
                    } catch (NumberFormatException e) {
                        // If win count is not a valid number, print an error.
                        System.err.println("Invalid win count format in statistics file: " + parts[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error if something goes wrong reading the file.
        }
    }

    // Saves the current win statistics to the "user_stats.txt" file.
    private void saveStats() {
        // 'false' means overwrite the file (not append).
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATS_FILE_NAME, false))) {
            // Go through each user and their win count.
            for (Map.Entry<String, Integer> entry : userWins.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue()); // Write username:wins.
                writer.newLine(); // Add a new line for the next user.
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error if something goes wrong writing to file.
        }
    }

    // Increases the win count for a user and saves it.
    public void incrementWin(String username) {
        // Get current wins for the user, or 0 if they have none.
        int currentWins = userWins.getOrDefault(username, 0);
        userWins.put(username, currentWins + 1); // Add 1 to their wins.
        saveStats(); // Save all stats to the file.
        System.out.println(username + " win count " + (currentWins + 1)); // Print new win count.
    }

}
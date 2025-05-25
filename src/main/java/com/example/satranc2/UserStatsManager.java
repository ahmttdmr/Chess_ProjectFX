package com.example.satranc2;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserStatsManager {
    // İstatistiklerin saklanacağı dosyanın adı.
    private static final String STATS_FILE_NAME = "user_stats.txt";
    // Kullanıcı adlarını ve kazanma sayılarını bellekte tutan harita (Map).
    private final Map<String, Integer> userWins = new HashMap<>();

    public UserStatsManager() {
        loadStats();
    }


    private void loadStats() {
        File file = new File(STATS_FILE_NAME);
        if (!file.exists()) {
            return; // Dosya henüz yoksa yüklenecek bir şey de yoktur.
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":"); // Satırı ":" karakterine göre ayır (kullanıcıadı:kazanmasayısı)
                if (parts.length == 2) {
                    String username = parts[0];
                    try {
                        int wins = Integer.parseInt(parts[1]);
                        userWins.put(username, wins); // Kullanıcıyı ve kazanma sayısını haritaya ekle
                    } catch (NumberFormatException e) {
                        System.err.println("İstatistik dosyasında geçersiz kazanma sayısı formatı: " + parts[1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Dosya okuma hatası olursa konsola yazdır.
        }
    }

    private void saveStats() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATS_FILE_NAME, false))) { // 'false' dosyanın üzerine yazılmasını sağlar
            for (Map.Entry<String, Integer> entry : userWins.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine(); // Her kullanıcı için yeni bir satır ekle.
            }
        } catch (IOException e) {
            e.printStackTrace(); // Dosya yazma hatası olursa konsola yazdır.
        }
    }

    /**
     * Belirtilen kullanıcının kazanma sayısını bir artırır ve dosyayı günceller.
     * @param username Kazanma sayısı artırılacak kullanıcı adı.
     */
    public void incrementWin(String username) {
        // Mevcut kazanma sayısını al, eğer kullanıcı listede yoksa varsayılan olarak 0 al.
        int currentWins = userWins.getOrDefault(username, 0);
        userWins.put(username, currentWins + 1); // Kazanma sayısını bir artır.
        saveStats(); // Değişikliği dosyaya kaydet.
        System.out.println(username + " adlı oyuncunun yeni kazanma sayısı: " + (currentWins + 1));
    }

    /**
     * Belirtilen bir kullanıcının kazanma sayısını döndürür.
     * @param username İstatistiği istenen kullanıcı.
     * @return Kullanıcının kazanma sayısı.
     */
    public int getWins(String username) {
        return userWins.getOrDefault(username, 0);
    }
}
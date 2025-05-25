# Chess ProjectFX

**Chess ProjectFX** is a fully-featured, two-player desktop chess game built with JavaFX.  
It implements all official FIDE rules, provides a modern GUI, user authentication, persistent statistics, and convenient game controls.

## Features

- **Complete Chess Logic**  
  - Pawn, Rook, Knight, Bishop, Queen, King hareketleri  
  - Special moves: Castling, Pawn Promotion, En Passant  
  - Otomatik Check & Checkmate tespiti

- **Intuitive GUI**  
  - Sürükle-bırak veya tıkla-taşı yöntemi  
  - Seçilen taşın yasal hamlelerinin vurgulanması  
  - Özelleştirilebilir tema ve ikon

- **User Management & Persistence**  
  - Kayıt / Giriş ekranı  
  - Oyuncu istatistiklerini (galibiyet sayısı vs.) `users.txt` altında saklama  
  - “Undo” işleviyle son hamleyi geri alabilme

## Prerequisites

- Java Development Kit (JDK) **11+**  
- JavaFX SDK (compatible with your JDK)  
- Maven **3.6+** (veya tercihe göre Gradle)  
- IDE: IntelliJ IDEA, Eclipse, vb.

## Installation & Run

1. Repository’yi klonla:  
   ```bash
   git clone https://github.com/ahmttdmr/Chess_ProjectFX.git
   cd Chess_ProjectFX

2. IDE’de Maven projesi olarak aç.
3. JavaFX’i ayarla:

   * VM seçeneklerine ekle:

     ```
     --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
     ```
4. Uygulamayı başlat:

   ```bash
   mvn clean javafx:run
   ```

## Usage

1. Uygulama açıldığında “Register” ile yeni kullanıcı oluştur veya varolanla “Login” ol.
2. Tahtada bir taşı seç; geçerli hamleler otomatik vurgulanır.
3. Taşı sürükle-bırak ya da tıkla-tıkla taşı.
4. Gerekirse “Undo” butonuyla son hamleyi geri al.

## Project Structure

```
src/
└── main/
    ├── java/com/example/satranc2/
    │   ├── ChessApp.java          ← Main uygulama sınıfı
    │   ├── Piece.java             ← Tüm taşların temel sınıfı
    │   ├── Pawn.java              ← Piyon hareket kuralları
    │   ├── Rook.java              ← Kale hareket kuralları
    │   ├── Knight.java            ← At hareket kuralları
    │   ├── Bishop.java            ← Fil hareket kuralları
    │   ├── Queen.java             ← Vezir hareket kuralları
    │   ├── King.java              ← Şah hareket kuralları
    │   ├── BoardManager.java      ← Tahta oluşturma ve güncelleme
    │   ├── MoveManager.java       ← Hamle geçerlilik kontrolü
    │   ├── LoginScreen.java       ← Giriş/Kayıt UI ve işlemleri
    │   ├── UserManager.java       ← Kullanıcı verisi okuma/yazma
    │   ├── UserStatsManager.java  ← İstatistiklerin saklanması
    │   └── MoveRecord.java        ← Undo için hamle kaydı
    └── resources/
        ├── fxml/                  ← FXML dosyaları
        └── images/                ← Taş ve ikon görüntüleri
```

![Chess ProjectFX Screenshot](assets/ChessApp.png)

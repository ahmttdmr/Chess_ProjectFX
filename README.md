JavaFX Chess Game

A fully-featured, two-player desktop chess game developed using JavaFX. This project provides a complete and interactive chess experience with a graphical user interface, user account management, persistent statistics, and full implementation of chess rules, including special moves.

(Not: Yukarıdaki ekran görüntüsü linkini kendi ekran görüntünüzle güncellemeyi unutmayın. Projenizin bir ekran görüntüsünü (örneğin ChessApp.png) repoya yükleyip linki düzenleyebilirsiniz.)

🌟 Key Features
This application goes beyond a simple chess engine by offering a rich set of features for an engaging user experience.

Complete Chess Logic:

All Piece Movements: All pieces (Pawn, Rook, Knight, Bishop, Queen, King) move according to official FIDE rules.
Special Moves: Full implementation of complex moves like Castling and Pawn Promotion.
Game State Detection: The engine automatically detects Check and Checkmate conditions to properly manage the game flow and determine the winner.
Interactive Graphical User Interface (GUI):

Visual Game Board: A clean and intuitive chessboard built with JavaFX.
Valid Move Highlighting: When a piece is selected, all its legal moves are visually marked on the board, guiding the player.
Drag-and-Drop or Click-to-Move: Supports modern UI interactions for moving pieces.
User Management and Persistence:

Authentication System: A dedicated LoginScreen provides a user-friendly interface for player registration and login.
Persistent Win Tracking: The UserStatsManager class records player victories, ensuring that user statistics are maintained across multiple game sessions.
Advanced Gameplay Features:

Undo Move: Players can take back their last move, thanks to a robust state management system that tracks every action.
Custom Application Icon: The application window features a custom icon for a polished and professional look.
🛠️ Technologies Used
Language: Java (JDK 11+ recommended)
Framework: JavaFX for the graphical user interface.
Build Tool: Maven / Gradle (Can be configured in any standard Java IDE).
🚀 How to Run
Follow these instructions to get the project running on your local machine.

Prerequisites:
Java Development Kit (JDK) version 11 or higher.
JavaFX SDK.
An IDE like IntelliJ IDEA or Eclipse.

Elbette, sağladığınız Java dosyalarını ve projenizin özelliklerini analiz ederek GitHub reponuz için profesyonel ve kapsamlı bir README.md dosyası hazırladım.

Aşağıdaki metni kopyalayıp doğrudan GitHub'daki README.md dosyanıza yapıştırabilirsiniz.

JavaFX Chess Game
A fully-featured, two-player desktop chess game developed using JavaFX. This project provides a complete and interactive chess experience with a graphical user interface, user account management, persistent statistics, and full implementation of chess rules, including special moves.

(Not: Yukarıdaki ekran görüntüsü linkini kendi ekran görüntünüzle güncellemeyi unutmayın. Projenizin bir ekran görüntüsünü (örneğin ChessApp.png) repoya yükleyip linki düzenleyebilirsiniz.)

🌟 Key Features
This application goes beyond a simple chess engine by offering a rich set of features for an engaging user experience.

Complete Chess Logic:

All Piece Movements: All pieces (Pawn, Rook, Knight, Bishop, Queen, King) move according to official FIDE rules.
Special Moves: Full implementation of complex moves like Castling and Pawn Promotion.
Game State Detection: The engine automatically detects Check and Checkmate conditions to properly manage the game flow and determine the winner.
Interactive Graphical User Interface (GUI):

Visual Game Board: A clean and intuitive chessboard built with JavaFX.
Valid Move Highlighting: When a piece is selected, all its legal moves are visually marked on the board, guiding the player.
Drag-and-Drop or Click-to-Move: Supports modern UI interactions for moving pieces.
User Management and Persistence:

Authentication System: A dedicated LoginScreen provides a user-friendly interface for player registration and login.
Persistent Win Tracking: The UserStatsManager class records player victories, ensuring that user statistics are maintained across multiple game sessions.
Advanced Gameplay Features:

Undo Move: Players can take back their last move, thanks to a robust state management system that tracks every action.
Custom Application Icon: The application window features a custom icon for a polished and professional look.
🛠️ Technologies Used
Language: Java (JDK 11+ recommended)
Framework: JavaFX for the graphical user interface.
Build Tool: Maven / Gradle (Can be configured in any standard Java IDE).
🚀 How to Run
Follow these instructions to get the project running on your local machine.

Prerequisites:
Java Development Kit (JDK) version 11 or higher.
JavaFX SDK.
An IDE like IntelliJ IDEA or Eclipse.
Steps:
Clone the repository:

Bash

git clone https://github.com/ahmttdmr/Chess_ProjectFX.git
Open in your IDE:

Open the cloned folder as a new project in IntelliJ IDEA or Eclipse.
The IDE should automatically detect the project structure.
Configure JavaFX:

Ensure your IDE's project structure is configured to use the JavaFX SDK.
You might need to add VM options to your run configuration if you are not using a modular setup. For example:
--module-path /path/to/your/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
(Replace /path/to/your/javafx-sdk/lib with the actual path on your system.)
Run the Application:

Locate the ChessApp.java file inside the src/main/java/com/example/satranc2 directory.
Run the main method in this file to start the application. The login screen should appear.
📂 Project Structure
The project follows a modular, object-oriented design to separate concerns.

ChessApp.java: The main application class. It orchestrates the game flow, GUI rendering, and user interactions.
Piece.java: An abstract base class for all chess pieces, defining common properties and behaviors.
King.java, Queen.java, etc.: Concrete subclasses of Piece, each implementing the unique isValidMove() logic for that piece.
LoginScreen.java: Manages the UI and logic for the user login and registration window.
UserManager.java: Handles the persistence of user account data (e.g., reading/writing to users.txt).
UserStatsManager.java: Manages the persistence of player statistics (e.g., win counts).
(Assumed) MoveRecord.java: A data class used to store the complete state of a move, enabling the "Undo" functionality.

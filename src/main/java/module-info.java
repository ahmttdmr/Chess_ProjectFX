module com.example.satranc2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.satranc2 to javafx.fxml;
    exports com.example.satranc2;
}
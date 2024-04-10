module com.example.parismapca2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.parismapca2 to javafx.fxml;
    exports com.example.parismapca2;
}
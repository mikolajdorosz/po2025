module org.example.simulatorgui {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.simulatorgui.controller to javafx.fxml;
    opens org.example.simulatorgui.controller.form to javafx.fxml;
    opens org.example.simulatorgui.controller.car to javafx.fxml;

    exports org.example.simulatorgui.app;
}

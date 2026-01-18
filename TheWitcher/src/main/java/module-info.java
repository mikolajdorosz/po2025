module org.example.thewitcher {
    requires javafx.controls;
    requires javafx.fxml;


    exports org.example.thewitcher.controller;
    opens org.example.thewitcher.controller to javafx.fxml;
    exports org.example.thewitcher.app;
    opens org.example.thewitcher.app to javafx.fxml;
    exports org.example.thewitcher.config;
    opens org.example.thewitcher.config to javafx.fxml;
}

module org.example.thewitcher {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.thewitcher to javafx.fxml;
    exports org.example.thewitcher;
    exports org.example.thewitcher.controller;
    opens org.example.thewitcher.controller to javafx.fxml;
}

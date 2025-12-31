module org.example.simulatorgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires Simulator;

    opens org.example.simulatorgui.controller to javafx.fxml;
    opens org.example.simulatorgui to javafx.fxml;
    exports org.example.simulatorgui;
    opens org.example.simulatorgui.controller.addcarform to javafx.fxml;
    opens org.example.simulatorgui.controller.maincomponent to javafx.fxml;
}

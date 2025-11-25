module org.example.symulatorgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.symulatorgui to javafx.fxml;
    exports org.example.symulatorgui;
}
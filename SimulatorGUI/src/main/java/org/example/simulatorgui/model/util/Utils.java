package org.example.simulatorgui.model.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextFormatter;
import org.example.simulatorgui.model.car.Car;

import java.util.Optional;

public class Utils {
    public static String formatTime(double seconds) {
        int mins = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        int millis = (int) ((seconds - ((int) seconds)) * 1000); // milliseconds
        return String.format("%02d:%02d:%03d", mins, secs, millis);
    }
    public static TextFormatter<String> createDecimalTextFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*(\\.\\d*)?") ? change : null;
        });
    }
    public static TextFormatter<String> createIntegerTextFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        });
    }
    public static double distance(Position a, Position b) { return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2)); }
    public static void showDuplicateAlert(String name, String item) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Duplicate " + name);
        alert.setHeaderText(name.substring(0, 1).toUpperCase() + name.substring(1) + " already exists");
        alert.setContentText("The" + name + " \"" + item + "\" is already used.");
        alert.showAndWait();
    }
    public static boolean showDeleteAlert(Car item) {
        String contentText = "Are you sure you want to delete this car?";
        Alert alert = new Alert(Alert.AlertType.WARNING, contentText, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Deleting " + item.getModel());
        alert.setHeaderText("Deleting" + item.toString() + "!");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
}

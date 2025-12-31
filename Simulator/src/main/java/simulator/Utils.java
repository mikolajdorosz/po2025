package simulator;

public class Utils {
    public static String formatTime(double seconds) {
        int mins = (int) (seconds / 60);
        int secs = (int) (seconds % 60);
        int millis = (int) ((seconds - ((int) seconds)) * 1000); // milliseconds
        return String.format("%02d:%02d:%03d", mins, secs, millis);
    }
}

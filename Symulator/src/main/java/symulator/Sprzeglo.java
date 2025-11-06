package symulator;

public class Sprzeglo extends Komponent {
    private boolean stanSprzegla;

    public void wcisnij() {
        stanSprzegla = true;
    }
    public void zwolnij() {
        stanSprzegla = false;
    }
}

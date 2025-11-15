package symulator;

public class Sprzeglo extends Komponent {
    private boolean stanSprzegla;

    public Sprzeglo(String nazwa, double waga, double cena, String producent, String model) {
        super(nazwa, waga, cena, producent, model);
        stanSprzegla = false;
    }

    public void wcisnij() {
        stanSprzegla = true;
    }
    public void zwolnij() {
        stanSprzegla = false;
    }
}

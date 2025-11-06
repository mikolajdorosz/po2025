package symulator;

public class Samochod {
    private Silnik silnik;
    private SkrzyniaBiegow skrzynia;
    private boolean stanWlaczenia;
    private String nrRejest;
    private String model;
    private int predkoscMax;

    public Samochod(Silnik _silnik, SkrzyniaBiegow _skrzynia, String _nrRejest, String _model, int _predkoscMax) {
        silnik = _silnik;
        skrzynia = _skrzynia;
        stanWlaczenia = false;
        nrRejest = _nrRejest;
        model = _model;
        predkoscMax = _predkoscMax;
    }

    public void wlacz() {
        silnik.uruchom();
        stanWlaczenia = true;
    }
    public void wylacz() {
        silnik.zatrzymaj();
        skrzynia.zmniejszBieg();
        stanWlaczenia = false;
    }
    public void jedzDo(Pozycja cel) {
        Pozycja aktualnaPozycja = new Pozycja();
        while (!aktualnaPozycja.getPozycja().equals(cel.getPozycja())) {

        }
    }

    public int getWaga() { return silnik.getWaga() + skrzynia.getWaga(); }
    public int getAktualnaPredkosc() {}
    public String getAktualnaPozycja() {}
}

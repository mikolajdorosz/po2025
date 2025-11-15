package symulator;

public class Samochod {
    private Pozycja aktualnaPozycja;
    private Silnik silnik;
    private SkrzyniaBiegow skrzynia;
    private boolean stanWlaczenia;
    private String nrRejest;
    private String model;
    private int predkoscMax;

    public Samochod(String nrRejest, String model, int predkoscMax, Pozycja pozycja, Silnik silnik, SkrzyniaBiegow skrzynia) {
        this.stanWlaczenia = false;
        this.nrRejest = nrRejest;
        this.model = model;
        this.predkoscMax = predkoscMax;
        this.aktualnaPozycja = pozycja;
        this.silnik = silnik;
        this.skrzynia = skrzynia;
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
    public void jedzDo(Pozycja cel) {}

    public double getWaga() { return silnik.getWaga() + skrzynia.getWaga(); }
    public int getAktualnaPredkosc() {
//        if (!stanWlaczenia) retrun 0;
//        else {
//            return
//        }
        return 1;
    }
    public String getAktualnaPozycja() { return aktualnaPozycja.getPozycja(); }
}

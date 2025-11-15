package symulator;

public class Silnik extends Komponent {
    private int maxObroty;
    private int obroty;
    private int deltaObroty;
    private int minObroty;

    public Silnik(int maxObroty, String nazwa, double waga, double cena, String producent, String model) {
        super(nazwa, waga, cena, producent, model);
        this.maxObroty = maxObroty;
        this.obroty = 0;
        this.deltaObroty = 250;
        this.minObroty = 1000;
    }

    public void uruchom() {
        obroty = minObroty;
    }
    public void zatrzymaj() {
        obroty = 0;
    }
    public void zwiekszObroty() {
        obroty += deltaObroty;
        if (obroty > maxObroty) {
            obroty = maxObroty;
        }
    }
    public void zmniejszObroty() {
        obroty -= deltaObroty;
        if (obroty < minObroty) {
            obroty = minObroty;
        }
    }
}

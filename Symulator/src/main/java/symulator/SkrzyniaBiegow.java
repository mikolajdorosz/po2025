package symulator;

public class SkrzyniaBiegow extends Komponent {
    private Sprzeglo sprzeglo;
    private int aktualnyBieg;
    private int iloscBiegow;
    private int aktualnePrzelozenie;

    public SkrzyniaBiegow(int iloscBiegow, String nazwa, int waga, int cena, String producent, String model, Sprzeglo sprzeglo) {
        super(nazwa, waga, cena, producent, model);
        this.sprzeglo = sprzeglo;
        this.aktualnyBieg = 1;
        this.iloscBiegow = iloscBiegow;
        this.aktualnePrzelozenie = 2;
    }

    public void zwiekszBieg() {
        sprzeglo.wcisnij();
        if (aktualnyBieg < iloscBiegow) {
            aktualnyBieg += 1;
            aktualnePrzelozenie -= 0.25;
        }
        sprzeglo.zwolnij();
    }
    public void zmniejszBieg() {
        sprzeglo.wcisnij();
        if (aktualnyBieg > 0) {
            aktualnyBieg -= 1;
            aktualnePrzelozenie += 0.25;
        }
        sprzeglo.zwolnij();
    }

    @Override
    public double getWaga() { return super.getWaga() + sprzeglo.getWaga(); }
    public int getAktualnyBieg() { return aktualnyBieg; }
    public int getAktualnePrzelozenie() { return aktualnePrzelozenie; }
}

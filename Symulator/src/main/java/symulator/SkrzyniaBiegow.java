package symulator;

public class SkrzyniaBiegow extends Komponent {
    private int aktualnyBieg;
    private int iloscBiegow;
    private int aktualnePrzelozenie;

    public void zwiekszBieg() { aktualnyBieg += 1; }
    public void zmniejszBieg() {aktualnePrzelozenie -= 1; }

    public int getAktualnyBieg() { return aktualnyBieg; }
    public int getAktualnePrzelozenie() { return aktualnePrzelozenie; }
}

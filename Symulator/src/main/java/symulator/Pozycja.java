package symulator;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Pozycja {
    private double x;
    private double y;

    public Pozycja() {
        this.x = 0;
        this.y = 0;
    }
    public Pozycja(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void aktualizujPozycje(double deltaX,  double deltaY) {
        x += deltaX;
        y += deltaY;
    }
    public String getPozycja() { return "X: " + x + ", Y: " + y; }
    public double getX() { return x; }
    public double getY() { return y; }
    public void przemiesc(Pozycja cel, double predkosc, double deltaCzas) {
        double krok = predkosc * deltaCzas;
        double deltaX = cel.getX() - this.getX();
        double deltaY = cel.getY() - this.getY();
        double droga = sqrt(pow(deltaX, 2) + pow(deltaY, 2));

        if (krok == 0 || droga == 0) return;
        if (krok > droga) krok = droga;
        aktualizujPozycje(krok * deltaX / droga,  krok * deltaY / droga);
    }
}

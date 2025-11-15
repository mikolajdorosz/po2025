package symulator;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PozycjaTest {

    @Test
    public void testRuchPoProstej() {
        Pozycja p = new Pozycja(0, 0);
        Pozycja cel = new Pozycja(10, 0);

        // krok 1
        p.przemiesc(cel, 3, 1);
        assertEquals(3.0, p.getX(), 1e-6);
        assertEquals(0.0, p.getY(), 1e-6);

        // krok 2
        p.przemiesc(cel, 3, 1);
        assertEquals(6.0, p.getX(), 1e-6);
        assertEquals(0.0, p.getY(), 1e-6);

        // krok 3
        p.przemiesc(cel, 3, 1);
        assertEquals(9.0, p.getX(), 1e-6);
        assertEquals(0.0, p.getY(), 1e-6);

        // krok 4 (do celu)
        p.przemiesc(cel, 3, 1);
        assertEquals(10.0, p.getX(), 1e-6);
        assertEquals(0.0, p.getY(), 1e-6);
    }

    @Test
    public void testRuchPoSkosie() {
        Pozycja p = new Pozycja(10, 0);
        Pozycja cel = new Pozycja(0, 10);

        p.przemiesc(cel, 5, 1);

        // ręczne wyliczenie kroków:
        // deltaX = -10, deltaY = 10
        // droga = sqrt(100 + 100) = 14.1421356237
        // przesunięcie:
        // krokX = 5 * (-10)/14.1421 ≈ -3.53553
        // krokY = 5 * 10 / 14.1421 ≈ 3.53553
        // nowa pozycja: X ≈ 6.46447, Y ≈ 3.53553

        assertEquals(6.464466, p.getX(), 1e-6);
        assertEquals(3.535533, p.getY(), 1e-6);
    }

    @Test
    public void testNiePrzesuwaSiePozerze() {
        Pozycja p = new Pozycja(5, 5);
        Pozycja cel = new Pozycja(5, 5);

        p.przemiesc(cel, 10, 1);
        assertEquals(5.0, p.getX(), 1e-6);
        assertEquals(5.0, p.getY(), 1e-6);
    }
}

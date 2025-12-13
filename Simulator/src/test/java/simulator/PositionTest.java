package simulator;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PositionTest {

    @Test
    public void straightMovement() {
        Position p = new Position(0, 0);
        Position destination = new Position(10, 0);

        // step 1
        p.replace(destination, 3, 1);
        assertEquals(3.0, p.getX(), 1e-6);
        assertEquals(0.0, p.getY(), 1e-6);

        // step 2
        p.replace(destination, 3, 1);
        assertEquals(6.0, p.getX(), 1e-6);
        assertEquals(0.0, p.getY(), 1e-6);

        // step 3
        p.replace(destination, 3, 1);
        assertEquals(9.0, p.getX(), 1e-6);
        assertEquals(0.0, p.getY(), 1e-6);

        // step 4 (to destination)
        p.replace(destination, 3, 1);
        assertEquals(10.0, p.getX(), 1e-6);
        assertEquals(0.0, p.getY(), 1e-6);
    }

    @Test
    public void diagonalMovement() {
        Position p = new Position(10, 0);
        Position destination = new Position(0, 10);

        p.replace(destination, 5, 1);

        // manual steps calculation:
        // dX = -10, dY = 10
        // s = sqrt(100 + 100) = 14.1421356237
        // displacement:
        // stepX = 5 * (-10)/14.1421 ≈ -3.53553
        // stepY = 5 * 10 / 14.1421 ≈ 3.53553
        // new position: X ≈ 6.46447, Y ≈ 3.53553

        assertEquals(6.464466, p.getX(), 1e-6);
        assertEquals(3.535533, p.getY(), 1e-6);
    }

    @Test
    public void zeroMovement() {
        Position p = new Position(5, 5);
        Position destination = new Position(5, 5);

        p.replace(destination, 10, 1);
        assertEquals(5.0, p.getX(), 1e-6);
        assertEquals(5.0, p.getY(), 1e-6);
    }
}

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;

public class CodingBatTest {

    @Test
    public void endUp() {
        assertEquals("HeLLO" , new CodingBat().endUp("Hello"));
        assertEquals("hi thERE" , new CodingBat().endUp("hi there"));
        assertEquals("HI" , new CodingBat().endUp("hi"));
    }

    @Test
    public void array667() {
        assertEquals(1 , new CodingBat().array667(new int[]{6, 6, 2}));
        assertEquals(1 , new CodingBat().array667(new int[]{6, 6, 2, 6}));
        assertEquals(1 , new CodingBat().array667(new int[]{6, 7, 2, 6}));
    }

    @Test
    public void evenOdd() {
        assertEquals("[0, 0, 0, 1, 1, 1, 1]", Arrays.toString(new CodingBat().evenOdd(new int[]{0, 0, 0, 1, 1, 1, 1})));
        assertEquals("[2, 3, 3]" , Arrays.toString(new CodingBat().evenOdd(new int[]{3, 3, 2})));
        assertEquals("[2, 2, 2]" , Arrays.toString(new CodingBat().evenOdd(new int[]{2, 2, 2})));
    }

    @Test
    public void withoutX2() {
        assertEquals("Hi", new CodingBat().withoutX2("xHi"));
        assertEquals("Hi", new CodingBat().withoutX2("Hxi"));
        assertEquals("Hi", new CodingBat().withoutX2("xHi"));
    }
}

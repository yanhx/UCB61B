import static org.junit.Assert.*;
import org.junit.Test;


public class FlikTest {

    @Test
    public void isSameNumberTest() {
        boolean a = Flik.isSameNumber(127, 127);
        assertTrue(a);
        boolean b = Flik.isSameNumber(128, 128);
        assertTrue(b);
        boolean c = Flik.isSameNumber(129, 129);
        assertTrue(c);
    }
}

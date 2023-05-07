package model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Test; 

/**
 * Unit test for {@link Coord} class
 */
public class CoordTest {

    @Test
    public void coordClassExists()
    {
        try {
            Class.forName("model.Coord");
        } catch (ClassNotFoundException e) {
            fail("Coord class does not exist");
        }
    }

    /**
     * Tests extraits de {@link model.TestModel}
     */
    @Test
    public void coordonneesValides_returnsAsExpected_quick() {
        String failMsgText = "Todo atelier 1 : (implémenter) coordonnees_valides()";
		Coord c1 = new Coord('a', 7);
        assertTrue(failMsgText, Coord.coordonnees_valides(c1));
        assertFalse(failMsgText, Coord.coordonnees_valides( new Coord('w',9)));
        assertFalse(failMsgText, Coord.coordonnees_valides( new Coord('b',11)));
    }
    
}

package eu.kartoffelquadrat.maxplayercolours;

import org.junit.jupiter.api.Test;

public class PlayColourUtilsTest {

    /**
     * Tests the creation of a set of size two, where the provided origin value is red: (255,0,0). Expected output is an
     * array of two with red and it's complementary colour (Cyan).
     */
    @Test
    public void testSetOfTwo() throws PlayerColourUtilsException {
        int[][] colours = PlayerColourUtils.generateColourSet(255, 0, 0, 2);
        assert colours.length == 2;
    }

}

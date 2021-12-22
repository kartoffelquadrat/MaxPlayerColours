package eu.kartoffelquadrat.maxplayercolours;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Various tests for the PlayColourUtils class.
 *
 * @author Maximilian Schiedermeier
 */
public class PlayColourUtilsTest {

    /**
     * Default constructor.
     */
    PlayColourUtilsTest() {
    }

    /**
     * Tests the creation of a set of size two, where the provided origin value is red: (255,0,0). Expected output is an
     * array of two with red and it's complementary colour (Cyan: 0,255,255).
     *
     * @throws PlayerColourUtilsException raises an exception if colour production fails. Is not expected to fail.
     */
    @Test
    public void testSetOfTwo() throws PlayerColourUtilsException {
        int[][] colours = PlayerColourUtils.generateColourSet(255, 0, 0, 2);

        // Verify two target colours were produced
        assert colours.length == 2;

        // Verify those are red and cyan
        // First colour should be red (255,0,0)
        assert colours[0][0] == 255;
        assert colours[0][1] == 0;
        assert colours[0][2] == 0;

        //Second colour should be cyan (0, 255, 255)
        assert colours[1][0] == 0;
        assert colours[1][1] == 255;
        assert colours[1][2] == 255;
    }

    /**
     * Tests the creation of a set of size two, where the provided origin value is unsaturated red: (5,0,0). Expected
     * output is an array of three with fully saturated red, green, blue.
     *
     * @throws PlayerColourUtilsException raises an exception if colour production fails. Is not expected to fail.
     */
    @Test
    public void testUnsaturatedSetOfThree() throws PlayerColourUtilsException {
        int[][] colours = PlayerColourUtils.generateColourSet(255, 0, 0, 3);

        // Verify two target colours were produced
        assert colours.length == 3;

        // Verify those are red and cyan
        // First colour should be red (255,0,0)
        assert colours[0][0] == 255;
        assert colours[0][1] == 0;
        assert colours[0][2] == 0;

        // Second colour should be green (0, 255, 0)
        assert colours[1][0] == 0;
        assert colours[1][1] == 255;
        assert colours[1][2] == 0;

        // Second colour should be blue (0, 0, 255)
        assert colours[2][0] == 0;
        assert colours[2][1] == 0;
        assert colours[2][2] == 255;
    }

    /**
     * Tests if various greyscale origin values are rejected.
     */
    @Test
    public void testGreyScaleInput() {

        // Test black
        Exception exception = assertThrows(PlayerColourUtilsException.class, () -> PlayerColourUtils.generateColourSet(0, 0, 0, 2));
        assertTrue(exception.getMessage().equals("Origin colour must have hue. (Greyscale not allowed)"));

        // Test greyscale
        exception = assertThrows(PlayerColourUtilsException.class, () -> PlayerColourUtils.generateColourSet(42, 42, 42, 2));
        assertTrue(exception.getMessage().equals("Origin colour must have hue. (Greyscale not allowed)"));

        // Test white
        exception = assertThrows(PlayerColourUtilsException.class, () -> PlayerColourUtils.generateColourSet(255, 255, 255, 2));
        assertTrue(exception.getMessage().equals("Origin colour must have hue. (Greyscale not allowed)"));
    }


    /**
     * Tests if input value with channel out of bounds is rejected.
     */
    @Test
    public void testOriginColourOutOfBounds() {

        // Test exceeding red
        Exception exception = assertThrows(PlayerColourUtilsException.class, () -> PlayerColourUtils.generateColourSet(1000, 0, 0, 2));
        assertTrue(exception.getMessage().equals(PlayerColourUtils.CHANNEL_OUT_OF_BOUNDS_ERROR_MESSAGE));

        // Test negative blue
        exception = assertThrows(PlayerColourUtilsException.class, () -> PlayerColourUtils.generateColourSet(0, 0, -42, 2));
        assertTrue(exception.getMessage().equals(PlayerColourUtils.CHANNEL_OUT_OF_BOUNDS_ERROR_MESSAGE));
    }

    /**
     * Tests if input value with channel out of bounds is rejected.
     */
    @Test
    public void testTargetColoursSizeOutOfBounds() {

        // Test not enough target colours
        Exception exception = assertThrows(PlayerColourUtilsException.class, () -> PlayerColourUtils.generateColourSet(255, 0, 0, 1));
        assertTrue(exception.getMessage().equals(PlayerColourUtils.TARGET_MIN_SIZE_ERROR_MESSAGE));

        // Test too many target colours
        exception = assertThrows(PlayerColourUtilsException.class, () -> PlayerColourUtils.generateColourSet(255, 0, 0, PlayerColourUtils.MAX_COLOUR_RANGE + 1));
        assertTrue(exception.getMessage().equals(PlayerColourUtils.TARGET_MAX_SIZE_ERROR_MESSAGE));

    }
}

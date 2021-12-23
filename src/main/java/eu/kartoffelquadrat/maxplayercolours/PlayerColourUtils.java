package eu.kartoffelquadrat.maxplayercolours;

import java.awt.*;

/**
 * Helper class to create human distinguishable set of colours. Generated colours have maximized saturation and value in
 * HSV room. The produces colours have maximized hue distance in HSB room.
 *
 * @author Maximilian Schiedermeier
 */
public class PlayerColourUtils {

    /**
     * Error message raised when a provided R/G/B input channel is out of bounds [0-255].
     */
    public static final String CHANNEL_OUT_OF_BOUNDS_ERROR_MESSAGE = "Origin colour must use rgb channels in range [0-255].";
    /**
     * Error message raised when the provided origin colour has no hue (is on greyscale pallet).
     */
    public static final String ORIGIN_IS_GREYSCALE_ERROR_MESSAGE = "Origin colour must have hue. (Greyscale not allowed)";
    /**
     * Error message raised when the requested target colour size is too small.
     */
    public static final String TARGET_MIN_SIZE_ERROR_MESSAGE = "Target array size must be at least 2.";
    /**
     * Error message raised when the requested target colour size is too big.
     */
    public static final String TARGET_MAX_SIZE_ERROR_MESSAGE = "Target colours are hard to distinguish for values > 8. Cowardly refusing to compute colours.";
    /**
     * Amount of hues at most produces (can be lifted, but then colours are harder to distinguish)
     */
    public static final int MAX_COLOUR_RANGE = 10;

    /**
     * Default Constructor. Not needed - the only required method (generateColourSet) is public/static.
     */
    private PlayerColourUtils() {
    }

    /**
     * Produces an array of n sRGB colours, based on a single origin sRGB colour. The origin colour will be maximized in
     * HSV saturation. All produced colours have also maximized HSB saturation and have a mutually maximized hue
     * distance.
     *
     * @param originR         int value for origin colour Red channel. Must be in range 0-255.
     * @param originG         int value for origin colour Green channel. Must be in range 0-255.
     * @param originB         int value for origin colour Blue channel. Must be in range 0-255.
     * @param targetArraySize int value to specify the amount of desired colours in the target colour array.
     * @return 2D-color array of size @{setSize}. Produces values are RGB triplets in sRGB space. First position
     * represents the max-saturated equivalent of the origin.
     * @throws PlayerColourUtilsException if a provided colour channel is out of range, the channels add up to a
     *                                    greyscale colour, or the target array size is not in valid range [2-8]
     */
    public static int[][] generateOptimalColours(int originR, int originG, int originB, int targetArraySize) throws PlayerColourUtilsException {

        // Reject invalid target-size requests
        if (targetArraySize < 2)
            throw new PlayerColourUtilsException(TARGET_MIN_SIZE_ERROR_MESSAGE);
        if (targetArraySize > MAX_COLOUR_RANGE)
            throw new PlayerColourUtilsException(TARGET_MAX_SIZE_ERROR_MESSAGE);

        // Reject if invalid origin colour (channels out of range / origin is on greyscale)
        verifyIsValidNonGreyScaleSRGB(originR, originG, originB);

        // Convert input value to HSB, and maximize saturation
        float originHue = extractHue(originR, originG, originB);

        // Produce the request amount of target colours (all maximized saturation, maximized mutual hue distance)
        return generateMaxDistanceSet(originHue, targetArraySize);
    }

    /**
     * Based on a single orig
     *
     * @param originHue       as the hue serving as fixpoint for the rotation shift of all produced colours on the HSB
     *                        hue circle.
     * @param targetArraySize as the amount of colours to produce (includes the origin).
     * @return 2D int array listing RGB triplets [0-255] per channel for all produced colours.
     */
    private static int[][] generateMaxDistanceSet(float originHue, int targetArraySize) {

        // target values must max out hue distance in colour space. Space available is 1.0, so we split it by the mount of requests colours.
        float optimalHueDistance = 1.0f / targetArraySize;

        // Create the requested amount of colours
        int[][] targetColours = new int[targetArraySize][];
        for (int i = 0; i < targetArraySize; i++) {

            // Compute target colour in HSB room
            float targetHue = (i * optimalHueDistance + originHue) % 1;

            // Convert to RGB and store in target array.
            targetColours[i] = new int[3];
            Color targetHSB = Color.getHSBColor(targetHue, 1.0f, 1.0f);
            targetColours[i][0] = targetHSB.getRed();
            targetColours[i][1] = targetHSB.getGreen();
            targetColours[i][2] = targetHSB.getBlue();
        }

        return targetColours;
    }

    /**
     * Helper method to verify if provided rgb channel values are allowed sRGB space. Greyscale input values are not
     * allowed, for their saturation can not be maximized.
     *
     * @param originR int value for origin colour Red channel. Must be in range 0-255.
     * @param originG int value for origin colour Green channel. Must be in range 0-255.
     * @param originB int value for origin colour Blue channel. Must be in range 0-255.
     * @throws PlayerColourUtilsException in case the provided origin RGB represents a colour without hue (a greyscale
     *                                    colour).
     */
    private static void verifyIsValidNonGreyScaleSRGB(int originR, int originG, int originB) throws PlayerColourUtilsException {

        // Verify that the individual channels are in correct range.
        boolean validChanelRanges = isValidChannelValue(originR) && isValidChannelValue(originB) && isValidChannelValue(originG);
        if (!validChanelRanges)
            throw new PlayerColourUtilsException(CHANNEL_OUT_OF_BOUNDS_ERROR_MESSAGE);

        // Verify the provided colour can be saturated (is not a greyscale colour).
        boolean greyScaleColour = (originR == originG) && (originG == originB);
        if (greyScaleColour)
            throw new PlayerColourUtilsException(ORIGIN_IS_GREYSCALE_ERROR_MESSAGE);
    }

    /**
     * Verifies if provided value is in range [0-255]
     *
     * @param value as the number to test for target range.
     * @return whether the provided value is in target range or not.
     */
    private static boolean isValidChannelValue(int value) {
        return value >= 0 && value <= 255;
    }


    /**
     * Boosts colour saturation (input provided in sRGB room) to maximum and returns the resulting HSB-hue value.
     *
     * @param originR int value for origin colour Red channel. Must be in range 0-255.
     * @param originG int value for origin colour Green channel. Must be in range 0-255.
     * @param originB int value for origin colour Blue channel. Must be in range 0-255.
     * @return float as target hue value in HSB room, if saturation and brightness of the provided sRBG input colour
     * boosted to max.
     */
    private static float extractHue(int originR, int originG, int originB) {

        // Convert input sRBG colour from cube to HSB room.
        float[] hsbColour = new float[3];
        Color.RGBtoHSB(originR, originG, originB, hsbColour);
        return hsbColour[0];
    }
}

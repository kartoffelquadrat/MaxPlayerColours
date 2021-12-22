package eu.kartoffelquadrat.maxplayercolours;

/**
 * Custom exception to indicate procession errors in MaxPlayerColours library.
 */
public class PlayerColourUtilsException extends Exception {

    /**
     * Default constructor that passes provided cause to super (exception) class.
     *
     * @param message cause for exception raised.
     */
    public PlayerColourUtilsException(String message) {
        super(message);
    }
}

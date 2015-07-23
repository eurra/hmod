
package hmod.core;

/**
 * Implements a general exception for the hMod library.
 * @author Enrique Urra C.
 */
public class AlgorithmException extends RuntimeException
{
    /**
     * Constructor, which provide an exception message.
     * @param message The message as String.
     */
    public AlgorithmException(String message)
    {
        super(message);
    }

    /**
     * Constructor, which builds from another exception.
     * @param thrwbl The base exception as throwable.
     */
    public AlgorithmException(Throwable thrwbl)
    {
        super(thrwbl);
    }

    /**
     * Constructor with a message and a parent exception.
     * @param string The message.
     * @param thrwbl The parent exception.
     */
    public AlgorithmException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }
}

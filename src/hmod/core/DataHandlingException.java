
package hmod.core;

/**
 * Implements a data-handling exception for the hMod library.
 * @author Enrique Urra C.
 */
public class DataHandlingException extends AlgorithmException
{
    /**
     * Constructor, which provide an exception message.
     * @param message The message as String.
     */
    public DataHandlingException(String message)
    {
        super(message);
    }

    /**
     * Constructor, which builds from another exception.
     * @param thrwbl The base exception as throwable.
     */
    public DataHandlingException(Throwable thrwbl)
    {
        super(thrwbl);
    }

    /**
     * Constructor with a message and a parent exception.
     * @param string The message.
     * @param thrwbl The parent exception.
     */
    public DataHandlingException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }
}

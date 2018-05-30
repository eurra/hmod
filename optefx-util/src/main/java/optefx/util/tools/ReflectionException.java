
package optefx.util.tools;

/**
 * Implements an exception for the hMod related to reflection operations.
 * @author Enrique Urra C.
 */
public class ReflectionException extends Exception
{
    /**
     * Constructor, which provide an exception message.
     * @param message The message as String.
     */
    public ReflectionException(String message)
    {
        super(message);
    }

    /**
     * Constructor, which builds from another exception.
     * @param thrwbl The base exception as throwable.
     */
    public ReflectionException(Throwable thrwbl)
    {
        super(thrwbl);
    }

    /**
     * Constructor with a message and a parent exception.
     * @param string The message.
     * @param thrwbl The parent exception.
     */
    public ReflectionException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }
}

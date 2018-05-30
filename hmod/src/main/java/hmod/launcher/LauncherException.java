
package hmod.launcher;

import hmod.core.AlgorithmException;

/**
 * Implements a exception for the hMod launcher.
 * @author Enrique Urra C.
 */
public class LauncherException extends AlgorithmException
{
    /**
     * Constructor, which provide an exception message.
     * @param message The message as String.
     */
    public LauncherException(String message)
    {
        super(message);
    }

    /**
     * Constructor, which builds from another exception.
     * @param thrwbl The base exception as throwable.
     */
    public LauncherException(Throwable thrwbl)
    {
        super(thrwbl);
    }

    /**
     * Constructor with a message and a parent exception.
     * @param string The message.
     * @param thrwbl The parent exception.
     */
    public LauncherException(String string, Throwable thrwbl)
    {
        super(string, thrwbl);
    }
}

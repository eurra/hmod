
package optefx.loader;

/**
 *
 * @author Enrique Urra C.
 */
public class ModuleException extends RuntimeException
{
    public ModuleException()
    {
    }
    
    public ModuleException(String message)
    {
        super(message);
    }

    public ModuleException(Throwable cause)
    {
        super(cause);
    }

    public ModuleException(String message, Throwable cause)
    {
        super(message, cause);
    }
}


package optefx.loader;

/**
 *
 * @author Enrique Urra C.
 */
public class ModuleLoadException extends ModuleException
{
    public ModuleLoadException()
    {
    }
    
    public ModuleLoadException(String message)
    {
        super(message);
    }

    public ModuleLoadException(Throwable cause)
    {
        super(cause);
    }

    public ModuleLoadException(String message, Throwable cause)
    {
        super(message, cause);
    }
}


package optefx.loader;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public class AmbiguousImplementationException extends ModuleException
{
    private final Class type;
    private final Object currImpl;
    private final Object newImpl;
    
    public AmbiguousImplementationException(Class type, Object currImpl, Object newImpl)
    {
        this.type = Objects.requireNonNull(type, "null type");
        this.currImpl = Objects.requireNonNull(currImpl, "null current implementation");
        this.newImpl = Objects.requireNonNull(newImpl, "null new implementation");
    }

    @Override
    public String getMessage()
    {
        return "Two different implementations were added for the type '" + type + "': '" + currImpl.getClass() + "' and '" + currImpl.getClass() + "'";
    }
}

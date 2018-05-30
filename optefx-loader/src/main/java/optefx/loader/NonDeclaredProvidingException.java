
package optefx.loader;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public class NonDeclaredProvidingException extends ModuleLoadException
{
    private final Class type;

    public NonDeclaredProvidingException(Class type)
    {
        this.type = Objects.requireNonNull(type, "null type");
    }

    @Override
    public String getMessage()
    {
        return "Trying to provide a non declared type (" + type + ")";
    }
    
    
}

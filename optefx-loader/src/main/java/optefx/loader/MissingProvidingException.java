
package optefx.loader;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public class MissingProvidingException extends ModuleLoadException
{
    private final Class type;
    private final Object loaderEntity;

    public MissingProvidingException(Class type, Object loaderEntity)
    {
        this.type = Objects.requireNonNull(type, "null type");
        this.loaderEntity = Objects.requireNonNull(loaderEntity, "null loader");
    }

    @Override
    public String getMessage()
    {
        return "The loader '" + loaderEntity + "' does not provide the type '" + type + "' as promised";
    }
    
    
}

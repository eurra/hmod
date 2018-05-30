
package optefx.loader;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public class UnmetDependencyException extends ModuleLoadException
{
    private final Class type;
    private final Object loaderEntity;

    public UnmetDependencyException(Class type, Object loaderEntity)
    {
        this.type = Objects.requireNonNull(type, "null type");
        this.loaderEntity = Objects.requireNonNull(loaderEntity, "null loader");
    }

    @Override
    public String getMessage()
    {
        return "The requested dependency (" + type + ") by the loader '" + loaderEntity + "' is not registered";
    }
}

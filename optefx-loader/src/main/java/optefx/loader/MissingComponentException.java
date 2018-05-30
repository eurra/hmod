
package optefx.loader;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public class MissingComponentException extends ModuleException
{
    private final Class type;

    public MissingComponentException(Class type)
    {
        this.type = Objects.requireNonNull(type, "null type");
    }

    @Override
    public String getMessage()
    {
        return "No implementation of the specified type (" + type + ") was loaded";
    }
}

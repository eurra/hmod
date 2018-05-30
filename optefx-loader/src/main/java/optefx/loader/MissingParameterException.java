
package optefx.loader;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public class MissingParameterException extends ModuleLoadException
{
    private final Parameter param;

    public MissingParameterException(Parameter param)
    {
        this.param = Objects.requireNonNull(param, "null parameter");
    }

    @Override
    public String getLocalizedMessage()
    {
        return "The parameter '" + param + "' was not provided";
    }
}

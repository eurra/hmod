
package optefx.loader;

import java.util.function.Consumer;

/**
 *
 * @author Enrique Urra C.
 */
public interface ParameterRegister
{
    <T> void addListener(Trigger<T> param, Consumer<T> listener);
    <T> void addBoundHandler(Resolvable<T> value, Consumer<T> handler);
    <T> T getValue(Parameter<T> param) throws ModuleLoadException;
    <T> T getRequiredValue(Parameter<T> param) throws ModuleLoadException;
    boolean isValueSet(Parameter param);
}

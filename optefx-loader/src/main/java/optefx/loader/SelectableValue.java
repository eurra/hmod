
package optefx.loader;

import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @author Enrique Urra C.
 */
public class SelectableValue<T> implements Resolvable<T>
{
    private final Class boundedType;
    private final Function mapProvider;

    public <K> SelectableValue(Class<K> boundedType, Function<K, Selector> selectorProvider)
    {
        this.boundedType = Objects.requireNonNull(boundedType, "null bounded type");
        this.mapProvider = Objects.requireNonNull(selectorProvider, "null selector provider");
    }

    @Override
    public final T resolve(Retriever ret) throws ModuleLoadException
    {
        if(!ret.contains(boundedType))
            throw new ModuleLoadException("Cannot retrieve a required bounded instance: '" + boundedType + "'");
        
        Object bounded = ret.get(boundedType);
        Function<Object, Selector> prov = mapProvider;
        Selector<Resolvable, T> map = prov.apply(bounded);
        
        return map.get(this);
    }
}

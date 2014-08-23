
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import hmod.core.DataHandler;

/**
 *
 * @author Enrique Urra C.
 */
public interface DataHandlerSetterBuilder<T> extends NestedBuilder<T>, DataHandlerSetterInput
{
    @Override DataHandlerSetterBuilder<T> setData(NestedBuilder<? extends DataHandler>... handlers);
}

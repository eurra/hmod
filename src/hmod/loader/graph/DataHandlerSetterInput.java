
package hmod.loader.graph;

import flexbuilders.core.Delegate;
import flexbuilders.core.NestedBuilder;
import hmod.core.DataHandler;

/**
 *
 * @author Enrique Urra C.
 */
public interface DataHandlerSetterInput extends Delegate
{
    DataHandlerSetterInput setData(NestedBuilder<? extends DataHandler>... handlers);
}

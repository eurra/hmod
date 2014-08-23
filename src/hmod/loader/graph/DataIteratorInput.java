
package hmod.loader.graph;

import flexbuilders.core.Delegate;
import flexbuilders.core.NestedBuilder;
import hmod.core.DataHandler;

/**
 *
 * @author Enrique Urra C.
 */
public interface DataIteratorInput extends Delegate
{
    DataIteratorInput addNext(NestedBuilder<? extends DataHandler> dataHandler);
}

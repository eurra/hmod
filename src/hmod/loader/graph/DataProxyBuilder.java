
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import hmod.core.DataHandler;

/**
 *
 * @author Enrique Urra C.
 */
public interface DataProxyBuilder extends NestedBuilder<DataHandler>, DataProxyInput
{
    @Override DataProxyBuilder addFactory(Object handlerFactory, NestedBuilder... creationArgs);
}

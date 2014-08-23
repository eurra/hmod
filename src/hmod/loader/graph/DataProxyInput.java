
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;

/**
 *
 * @author Enrique Urra C.
 */
public interface DataProxyInput extends Delegate
{
    DataProxyInput addFactory(Object handlerFactory, NestedBuilder... creationArgs);
}

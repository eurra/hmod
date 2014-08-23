
package hmod.loader.graph;

import flexbuilders.core.Delegate;
import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface ArgumentFactoryInput extends Delegate
{
    ArgumentFactoryInput nextArg(NestedBuilder arg);
}

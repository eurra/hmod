
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface ArgumentFactoryBuilder<T> extends NestedBuilder<T>, ArgumentFactoryInput
{
    @Override ArgumentFactoryBuilder<T> nextArg(NestedBuilder arg);
}

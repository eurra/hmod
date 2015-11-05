
package hmod.solvers.common;

import java.util.Collection;

/**
 *
 * @author Enrique Urra C.
 */
public interface Selector<T>
{
    RouletteSelector<T> addAll(Collection<T> elements);
    RouletteSelector<T> addElement(T element);
    RouletteSelector<T> clear();
    boolean isEmpty();
    RouletteSelector<T> removeElement(T elem);
    int getElementsCount();
    T select();
}

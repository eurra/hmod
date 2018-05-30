
package hmod.core;

import java.util.function.Supplier;

/**
 *
 * @author Enrique Urra C.
 */
public interface Expression<T> extends Supplier<T>
{
    @Override public T get() throws AlgorithmException;
}

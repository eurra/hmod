
package hmod.core;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 * @param <T>
 */
public abstract class ComposableStatement<T extends Statement> implements Statement
{    
    @Override
    public final void run() throws AlgorithmException
    {
        checkComposed().run();
    }

    @Override
    public final void stop()
    {
        checkComposed().stop();
    }

    @Override
    public final String toString()
    {
        return checkComposed().toString();
    }
    
    private T checkComposed()
    {
        return Objects.requireNonNull(get(), "the composed statement has not been set");
    }
    
    protected abstract T get();
}

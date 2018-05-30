
package hmod.core;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public interface Statement extends Runnable
{
    public static final Statement EMPTY_STATEMENT = () -> {};
    
    public static Statement from(Runnable run) 
    {
        Objects.requireNonNull(run, "null runnable");
        return () -> run.run();
    }
    
    @Override public void run() throws AlgorithmException;
    
    default void stop() throws AlgorithmException
    {
    }
}

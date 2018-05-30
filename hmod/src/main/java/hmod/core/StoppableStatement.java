
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class StoppableStatement implements Statement
{
    private boolean running;
    
    protected final void start()
    {
        running = true;
    }
    
    @Override
    public void stop()
    {
        running = false;
    }

    protected final boolean isRunning()
    {
        return running;
    }
}

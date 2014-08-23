
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class AbstractAlgorithm implements Algorithm
{
    private boolean stopped;
    
    protected final void runStep(Step startStep)
    {
        if(stopped)
            return;
        
        Step currStep = startStep;

        while(currStep != null && !stopped)
            currStep = currStep.resolveNext();
    }

    @Override
    public final void start() throws AlgorithmException
    {
        stopped = false;
        launch();
    }

    @Override
    public final void stop() throws AlgorithmException
    {
        stopped = true;
    }
    
    protected abstract void launch();
}

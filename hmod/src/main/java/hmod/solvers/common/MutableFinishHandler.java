
package hmod.solvers.common;

/**
 *
 * @author Enrique Urra C.
 */
class MutableFinishHandler implements FinishHandler
{
    private boolean finished;

    public MutableFinishHandler()
    {
    }

    public void finishHeuristicManually()
    {
        finished = true;
    }
    
    public void restartFinishFlag()
    {
        finished = false;
    }

    @Override
    public boolean isManuallyFinished()
    {
        return finished;
    }    
}

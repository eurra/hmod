
package hmod.solvers.common.ac;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultFinishHandler implements FinishHandler
{
    private boolean finished;

    @Override
    public void finish()
    {
        finished = true;
    }

    @Override
    public boolean isFinished()
    {
        return finished;
    }
    
}

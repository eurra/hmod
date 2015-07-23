
package hmod.solvers.common;

/**
 *
 * @author Enrique Urra C.
 */
public final class MutableIterationHandler implements IterationHandler
{
    private int maxIteration;
    private int currIteration;

    public MutableIterationHandler()
    {
        this(1);
    }
    
    public MutableIterationHandler(int maxIteration)
    {
        this.maxIteration = maxIteration;
    }
    
    @Override
    public boolean areIterationsFinished()
    {
        return maxIteration > 0 && currIteration >= maxIteration;
    }

    @Override
    public int getMaxIteration()
    {
        return maxIteration;
    }

    @Override
    public int getCurrentIteration()
    {
        return currIteration;
    }

    public void advanceIteration()
    {
        currIteration++;
    }

    public void resetIterations()
    {
        currIteration = 0;
    }

    public void setMaxIterations(int value)
    {
        maxIteration = value;
        resetIterations();
    }
}

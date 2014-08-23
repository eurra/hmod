
package hmod.solvers.common.ac;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultIterationHandler implements IterationHandler
{
    private int maxIteration;
    private int currIteration;

    public DefaultIterationHandler(int maxIteration)
    {
        this.maxIteration = maxIteration;
    }

    @Override
    public boolean isIterationsFinished()
    {
        return maxIteration != -1 && currIteration >= maxIteration;
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

    @Override
    public void advanceIteration()
    {
        currIteration++;
    }

    @Override
    public void resetIterations()
    {
        resetIterations(maxIteration);
    }

    @Override
    public void resetIterations(int newMaxIterations)
    {
        currIteration = 0;
        
        if(newMaxIterations != maxIteration)
            maxIteration = newMaxIterations;
    }
    
}

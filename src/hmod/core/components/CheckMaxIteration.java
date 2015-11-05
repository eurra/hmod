
package hmod.core.components;

import hmod.core.BooleanOperator;

/**
 * Implements an operator that holds an iteration count and updates it each 
 * time it is executed. The result generated is true if the iteration count is 
 * below the maximum value specified.
 * @author Enrique Urra C.
 */
public class CheckMaxIteration extends BooleanOperator
{
    protected IterationData iterationHandler;
    
    public void setIterationData(IterationData data)
    {
        this.iterationHandler = data;
    }

    @Override
    public Boolean evaluate()
    {
        int nextIteration = iterationHandler.getCurrentIteration() + 1;
        int maxIteration = iterationHandler.getMaxIteration();
        iterationHandler.setCurrentIteration(nextIteration);
        boolean finished = maxIteration != -1 && nextIteration >= maxIteration;
        iterationHandler.setIterationFinished(finished);
        
        return finished;
    }
}

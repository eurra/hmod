
package hmod.core.components;

import hmod.core.Operator;
import hmod.core.AlgorithmException;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class ResetIterationOperator implements Operator
{
    protected IterationData iterationHandler;

    public void setIterationData(IterationData data)
    {
        this.iterationHandler = data;
    }
    
    @Override
    public final void execute() throws AlgorithmException
    {        
        iterationHandler.setCurrentIteration(0);
        iterationHandler.setMaxIteration(getMaxIteration());
        iterationHandler.setIterationFinished(false);
    }
    
    public abstract int getMaxIteration() throws AlgorithmException;
}

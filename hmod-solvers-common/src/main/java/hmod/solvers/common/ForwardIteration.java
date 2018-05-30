
package hmod.solvers.common;

import hmod.ap.FindOperations;
import hmod.ap.Operator;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public final class ForwardIteration implements Iteration
{
    private int max;
    private int curr;

    public ForwardIteration()
    {
        this(1);
    }
    
    public ForwardIteration(int max)
    {
        this.max = max;
    }
    
    @Override
    public boolean isFinished()
    {
        return max > 0 && curr >= max;
    }

    @Override
    public int getMax()
    {
        return max;
    }

    @Override
    public int getCurrent()
    {
        return curr;
    }

    @Operator 
    public void advance()
    {
        curr++;
    }
}

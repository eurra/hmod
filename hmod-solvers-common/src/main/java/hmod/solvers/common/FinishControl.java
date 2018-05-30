
package hmod.solvers.common;

import hmod.ap.FindOperations;
import hmod.ap.Operator;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public class FinishControl
{    
    private boolean finished;

    @Operator 
    public void triggerFinish()
    {
        finished = true;
    }

    @Operator 
    public boolean isFinished()
    {
        return finished;
    }    
}

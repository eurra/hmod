
package hmod.core.components;

import hmod.core.Operator;
import hmod.core.AlgorithmException;

/**
 *
 * @author Enrique Urra C.
 */
public class StartTime implements Operator
{
    protected TimeElapsedData timeElapsedHandler;

    public void setTimeElapsedData(TimeElapsedData timeElapsedHandler)
    {
        this.timeElapsedHandler = timeElapsedHandler;
    }
    
    @Override
    public void execute() throws AlgorithmException
    {
        timeElapsedHandler.setInitTime(System.currentTimeMillis());
        timeElapsedHandler.setTimeFinished(false);
    }
}

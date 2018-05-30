
package hmod.solvers.common;

import hmod.ap.FindOperations;
import hmod.ap.Operator;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public final class SwitchTimeElapsed implements TimeElapsed
{    
    private double maxSeconds;
    private double initTime;
    private double finalSeconds;

    public SwitchTimeElapsed()
    {
        this(-1.0);
    }
    
    public SwitchTimeElapsed(double maxSeconds)
    {
        this.maxSeconds = maxSeconds;
    }

    @Operator 
    public void startTime()
    {
        finalSeconds = 0.0;
        initTime = System.currentTimeMillis();
    }
    
    @Operator 
    public void endTime()
    {
        finalSeconds = getElapsedSeconds();
    }

    @Override
    public double getMaxSeconds()
    {
        return maxSeconds;
    }

    @Override
    public double getElapsedSeconds()
    {
        long currTime = System.currentTimeMillis();
        double diffTime = currTime - initTime;
        return diffTime / 1000.0;
    }

    @Override
    public double getFinalSeconds()
    {
        return finalSeconds;
    }

    @Override
    public boolean isFinished()
    {
        return maxSeconds > 0.0 && getElapsedSeconds() >= maxSeconds;
    }
}

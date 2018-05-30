
package hmod.solvers.common;

/**
 *
 * @author Enrique Urra C.
 */
public final class MutableTimeElapsedHandler implements TimeElapsedHandler
{
    private double maxSeconds;
    private double initTime;
    private double finalSeconds;

    public MutableTimeElapsedHandler()
    {
        this(-1.0);
    }
    
    public MutableTimeElapsedHandler(double maxSeconds)
    {
        this.maxSeconds = maxSeconds;
    }
    
    public void setMaxSeconds(double value)
    {
        this.maxSeconds = value;
    }

    public void startTime()
    {
        finalSeconds = 0.0;
        initTime = System.currentTimeMillis();
    }
    
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
    public boolean isTimeFinished()
    {
        return maxSeconds > 0.0 && getElapsedSeconds() >= maxSeconds;
    }
}

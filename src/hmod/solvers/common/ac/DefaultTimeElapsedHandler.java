
package hmod.solvers.common.ac;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultTimeElapsedHandler implements TimeElapsedHandler
{
    private final double maxSeconds;
    private double initTime;

    public DefaultTimeElapsedHandler(double maxSeconds)
    {
        this.maxSeconds = maxSeconds;
    }

    @Override
    public void startElapsedTime()
    {
        initTime = System.currentTimeMillis();
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
    public boolean isElapsedTimeFinished()
    {
        return maxSeconds != -1.0 && getElapsedSeconds() >= maxSeconds;
    }
    
}

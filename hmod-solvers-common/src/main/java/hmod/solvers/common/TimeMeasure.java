
package hmod.solvers.common;

import hmod.ap.FindOperations;
import hmod.ap.Operator;
import java.util.concurrent.TimeUnit;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
class TimeMeasure
{        
    private long timespan;
    private long sum;
    private int numReadings;
    private final Iteration ih;
    private int lastShownIteration;
    private final int delay;

    public TimeMeasure(Iteration ih, int delay)
    {
        this.ih = ih;
        this.delay = delay;
    }

    @Operator
    public void init()
    {
        timespan = System.currentTimeMillis();
    }

    @Operator
    public void addReading()
    {
        long currTime = System.currentTimeMillis();
        sum += currTime - timespan;
        timespan = currTime;
        numReadings++;
    }

    public long avgTime()
    {
        return (long)((double)sum / numReadings);
    }

    @Operator
    public void showMessage()
    {
        long avgTime = avgTime();
        long estimatedTime = ih.getMax() * avgTime;

        String time = String.format("%d min, %d sec", 
            TimeUnit.MILLISECONDS.toMinutes(estimatedTime),
            TimeUnit.MILLISECONDS.toSeconds(estimatedTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(estimatedTime))
        );

        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
            "Iteration time (avg.): " + avgTime + " ms, " +
            "Estimated run time: " + time
        );

        lastShownIteration = ih.getCurrent();
    }

    @Operator
    public boolean checkMessageShow()
    {
        return ih.getCurrent() - lastShownIteration >= delay;
    }
}

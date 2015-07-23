
package hmod.solvers.common;

import static hmod.core.FlowchartFactory.*;
import java.util.concurrent.TimeUnit;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class IterationTimeMeasuring
{
    public static final Parameter<Integer> ITERATIONS_DELAY = new Parameter<>("IterationTimeMeasuring.ITERATIONS_DELAY");
    
    private static class Data
    {
        private long timespan;
        private long sum;
        private int numReadings;
        private final IterationHandler ih;
        private int lastShownIteration;
        private final int delay;

        public Data(IterationHandler ih, int delay)
        {
            this.ih = ih;
            this.delay = delay;
        }

        public void init()
        {
            timespan = System.currentTimeMillis();
        }
        
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
        
        public void showMessage()
        {
            long avgTime = avgTime();
            long estimatedTime = ih.getMaxIteration() * avgTime;
            
            String time = String.format("%d min, %d sec", 
                TimeUnit.MILLISECONDS.toMinutes(estimatedTime),
                TimeUnit.MILLISECONDS.toSeconds(estimatedTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(estimatedTime))
            );
            
            OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
                "Iteration time (avg.): " + avgTime + " ms, " +
                "Estimated run time: " + time
            );
            
            lastShownIteration = ih.getCurrentIteration();
        }
        
        public boolean checkMessageShow()
        {
            return ih.getCurrentIteration() - lastShownIteration >= delay;
        }
    }
    
    @LoadsComponent
    public static void load(ParameterRegister pr,
                            IterativeHeuristic iHeu,
                            IterationHandler ih)
    {
        int delay = pr.isValueSet(ITERATIONS_DELAY) ? pr.getValue(ITERATIONS_DELAY) : 50;        
        Data data = new Data(ih, delay);        
        
        iHeu.init().appendAfter(data::init);
        
        iHeu.initReporting().append(
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Showing iterations delay every " + delay + " iterations")
        );
        
        iHeu.iteration().appendAfter(block(
            data::addReading,
            If(data::checkMessageShow).then(
                data::showMessage
            )
        ));
    }
}

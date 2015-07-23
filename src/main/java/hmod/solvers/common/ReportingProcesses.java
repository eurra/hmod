
package hmod.solvers.common;

import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
class ReportingProcesses
{
    public static void printRandomSeed()
    {
        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
            "Random seed: " + RandomTool.getInstance().getSeed()
        );
    }
    
    private final IterationHandler ih;
    private final TimeElapsedHandler teh;
    
    public ReportingProcesses(IterationHandler ih,
                              TimeElapsedHandler teh)
    {
        this.ih = ih;
        this.teh = teh;
    }
    
    public void printMaxSeconds()
    {
        double maxSeconds = teh.getMaxSeconds();
        
        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
            "Max. execution time (sec.): " + (maxSeconds <= 0.0 ? "non-set" : maxSeconds)
        );
    }
    
    public void printTotalSeconds()
    {
        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
            "Total execution time (sec.): " + teh.getElapsedSeconds()
        );
    }
    
    public void printMaxIterations()
    {
        int maxIteration = ih.getMaxIteration();
        
        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
            "Max. iterations: " + (maxIteration <= 0 ? "non-set" : maxIteration)
        );
    }
}

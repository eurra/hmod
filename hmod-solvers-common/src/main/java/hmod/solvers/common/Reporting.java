
package hmod.solvers.common;

import hmod.ap.FindOperations;
import hmod.ap.Operator;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
class Reporting
{
    @Operator
    public static void printRandomSeed()
    {
        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
            "Random seed: " + RandomTool.getInstance().getSeed());
    }
    
    @Operator
    public static void printMaxSeconds(TimeElapsed te)
    {
        double maxSeconds = te.getMaxSeconds();

        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
            "Max. execution time (sec.): " + (maxSeconds <= 0.0 ? "non-set" : maxSeconds)
        );
    }
    
    @Operator
    public static void printTotalSeconds(TimeElapsed te)
    {
        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
            "Total execution time (sec.): " + te.getElapsedSeconds()
        );
    }
    
    @Operator
    public static void printMaxIterations(Iteration it)
    {
        int maxIteration = it.getMax();
        
        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, 
            "Max. iterations: " + (maxIteration <= 0 ? "non-set" : maxIteration)
        );
    }

    private Reporting()
    {
    }
}

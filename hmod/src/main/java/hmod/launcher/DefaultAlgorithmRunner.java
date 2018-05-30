
package hmod.launcher;

import hmod.core.AlgorithmException;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultAlgorithmRunner extends AlgorithmRunner
{
    @Override
    public void runAlgorithm(Runnable algorithm)
    {
        try
        {
            OutputManager.println(OutputManager.DEFAULT_ID, "Starting execution...");
            long start = System.currentTimeMillis();

            algorithm.run();

            long end = System.currentTimeMillis();
            OutputManager.println(OutputManager.DEFAULT_ID, "Execution Finished (total time: " + ((end - start) / 1000.0f) + "s).");
        }
        catch(AlgorithmException ex)
        {
            OutputManager.print(OutputManager.DEFAULT_ERROR_ID, "ALGORITHM ERROR: ");
            ex.printStackTrace(OutputManager.getCurrent().getOutput(OutputManager.DEFAULT_ERROR_ID));
        }
        catch(RuntimeException ex)
        {
            OutputManager.print(OutputManager.DEFAULT_ERROR_ID, "RUNTIME ERROR: ");
            ex.printStackTrace(OutputManager.getCurrent().getOutput(OutputManager.DEFAULT_ERROR_ID));
        }
    }
}
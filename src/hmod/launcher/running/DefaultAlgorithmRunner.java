
package hmod.launcher.running;

import hmod.core.Algorithm;
import hmod.core.AlgorithmException;
import hmod.launcher.Launcher;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultAlgorithmRunner extends AlgorithmRunner
{
    @Override
    public void runAlgorithm(Algorithm algorithm)
    {
        try
        {
            OutputManager.println(Launcher.OUT_COMMON, "Starting execution...");
            long start = System.currentTimeMillis();

            algorithm.start();

            long end = System.currentTimeMillis();
            OutputManager.println(Launcher.OUT_COMMON, "Execution Finished (total time: " + ((end - start) / 1000.0f) + "s).");
        }
        catch(AlgorithmException ex)
        {
            OutputManager.print(Launcher.OUT_ERROR, "Algorithm Error: ");
            ex.printStackTrace(OutputManager.getCurrent().getOutput(Launcher.OUT_ERROR));
        }
    }
}

package hmod.launcher;

import hmod.core.Statement;
import optefx.util.output.OutputConfig;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultAlgorithmInterface implements AlgorithmInterface
{ 
    @Override
    public Runnable configure(Statement algorithm, OutputConfig outputConfigBuilder)
    {
        return () -> algorithm.run();
    }

    @Override
    public void start(Thread launcherThread)
    {
        try
        {
            launcherThread.join();
        }
        catch(InterruptedException ex)
        {
        }
    }
}

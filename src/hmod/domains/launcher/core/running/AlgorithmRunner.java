
package hmod.domains.launcher.core.running;

import hmod.core.Algorithm;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class AlgorithmRunner
{
    private class InnerThread extends Thread
    {
        private Algorithm algorithm; 
        private OutputConfig outputConfig;
        private long randomSeed = -1;
        
        @Override
        public void run()
        {
            if(outputConfig != null)
                OutputManager.getCurrent().setOutputsFromConfig(outputConfig);
        
            if(randomSeed != -1)
                RandomTool.getInstance().setSeed(randomSeed);

            try
            {
                runAlgorithm(algorithm);
            }
            catch(RuntimeException ex)
            {
                OutputManager.print(OutputManager.DEFAULT_ERROR_ID, "UNHANDLED RUN EXCEPTION: ");
                ex.printStackTrace(OutputManager.getCurrent().getOutput(OutputManager.DEFAULT_ERROR_ID));
            }

            if(outputConfig != null)
            {
                OutputManager.getCurrent().closeOutputs();
                OutputManager.getCurrent().clearOutputs();
            }
        }
    }
    
    private InnerThread thread;

    public AlgorithmRunner()
    {
        thread = new InnerThread();
    }

    public Thread getAlgorithmThread()
    {
        return thread;
    }
    
    public void init(Algorithm algorithm, OutputConfig config, long seed)
    {
        thread.algorithm = algorithm;
        thread.outputConfig = config;
        thread.randomSeed = seed;
        
        thread.start();
    }
    
    protected abstract void runAlgorithm(Algorithm algorithm);
}
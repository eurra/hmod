
package hmod.launcher;

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
        private final Runnable starter; 
        private final OutputConfig outputConfig;
        private long randomSeed = -1;

        public InnerThread(Runnable starter, OutputConfig outputConfig, long randomSeed)
        {
            this.starter = starter;
            this.outputConfig = outputConfig;
            this.randomSeed = randomSeed;
        }
        
        @Override
        public void run()
        {
            if(outputConfig != null)
                OutputManager.getCurrent().setOutputsFromConfig(outputConfig);
        
            if(randomSeed != -1)
                RandomTool.getInstance().setSeed(randomSeed);

            try
            {
                runAlgorithm(starter);
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

    public Thread getAlgorithmThread(OutputConfig config, long seed, Runnable uiStarter)
    {
        return new InnerThread(uiStarter, config, seed);
    }
    
    protected abstract void runAlgorithm(Runnable algorithm);
}
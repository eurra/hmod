
package hmod.launcher.plugins;

import hmod.launcher.LauncherException;
import hmod.launcher.Launcher;
import hmod.launcher.Plugin;
import hmod.launcher.RunManager;
import hmod.launcher.components.RunData;
import hmod.launcher.running.AlgorithmInterfaceFactory;
import hmod.launcher.running.AlgorithmRunnerFactory;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public class RunLoaderPlugin implements Plugin
{
    private RunData runHandler;

    public void setRunData(RunData handler)
    {
        this.runHandler = handler;
    }
    
    @Override
    public void runPlugin() throws LauncherException
    {
        RunManager manager = new RunManager();
        runHandler.setRunManager(manager);
        
        OutputManager.print(Launcher.OUT_COMMON, "Checking algorithm runners registered... ");
        AlgorithmRunnerFactory[] runnerFactories = runHandler.getRunnerFactories();
        checkRunnerFactories(runnerFactories, manager);
        OutputManager.println(Launcher.OUT_COMMON, "done.");
        
        OutputManager.print(Launcher.OUT_COMMON, "Checking algorithm interfaces registered... ");
        AlgorithmInterfaceFactory[] interfaceFactories = runHandler.getInterfaceFactories();
        checkInterfaceFactories(interfaceFactories, manager);
        OutputManager.println(Launcher.OUT_COMMON, "done.");
        
        if(!manager.setCurrentRunnerFactory(runHandler.getDefaultRunner()))
            OutputManager.println(Launcher.OUT_COMMON, "Error: the default algorithm runner configured is invalid. Check your launcher configuration file.");
        
        if(!manager.setCurrentInterfaceFactory(runHandler.getDefaultInterface()))
            OutputManager.println(Launcher.OUT_COMMON, "Error: the default algorithm interface configured is invalid. Check your launcher configuration file.");
    }
    
    private void checkRunnerFactories(AlgorithmRunnerFactory[] factories, RunManager manager)
    {
        for(int i = 0; i < factories.length; i++)
        {
            AlgorithmRunnerFactory currFactory = factories[i];
            
            try
            {
                manager.registerRunnerFactory(currFactory);
            }
            catch(LauncherException ex)
            {
                OutputManager.print(Launcher.OUT_COMMON, "\nAlgorithm interface factory '" + currFactory.getClass().getCanonicalName() + "' skipped: " + ex.getLocalizedMessage() + "\n");
            }
        }
    }
    
    private void checkInterfaceFactories(AlgorithmInterfaceFactory[] factories, RunManager manager)
    {
        for(int i = 0; i < factories.length; i++)
        {
            AlgorithmInterfaceFactory currFactory = factories[i];
            
            try
            {
                manager.registerInterfaceFactory(currFactory);
            }
            catch(LauncherException ex)
            {
                OutputManager.print(Launcher.OUT_COMMON, "\nAlgorithm interface factory '" + currFactory.getClass().getCanonicalName() + "' skipped: " + ex.getLocalizedMessage() + "\n");
            }
        }
    }
}

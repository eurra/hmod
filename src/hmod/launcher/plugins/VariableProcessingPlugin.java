
package hmod.launcher.plugins;

import hmod.launcher.LauncherException;
import hmod.launcher.Launcher;
import hmod.launcher.Plugin;
import hmod.launcher.VariableProcessor;
import hmod.launcher.VariableManager;
import hmod.launcher.components.VariableData;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public class VariableProcessingPlugin implements Plugin
{
    private VariableData variableHandler;

    public void setVariableData(VariableData handler)
    {
        this.variableHandler = handler;
    }
    
    @Override
    public void runPlugin() throws LauncherException
    {        
        VariableManager manager = new VariableManager(variableHandler.getVariableDelimiter());
        variableHandler.setVariableManager(manager);
        
        OutputManager.print(Launcher.OUT_COMMON, "Loading variable processors... ");
        loadProcessors(manager, variableHandler.getVariableProcessors());
        
        OutputManager.println(Launcher.OUT_COMMON, "done.");
    }
    
    private void loadProcessors(VariableManager manager, VariableProcessor[] processors)
    {
        VariableProcessor currProcessor = null;
        
        for(VariableProcessor processor : processors)
        {
            currProcessor = processor;
        
            try
            {
                manager.addProcessor(processor);
            }
            catch(LauncherException ex)
            {
                OutputManager.print(Launcher.OUT_COMMON, "\nError loading variable processor '" + currProcessor.getClass().getCanonicalName() + "': " + ex.getMessage() + "\n");
            }
        }        
    }
}

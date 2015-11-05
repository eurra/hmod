
package hmod.launcher.components;

import hmod.core.AlgorithmException;
import hmod.core.Operator;
import hmod.launcher.Launcher;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public class EndLauncher implements Operator
{    
    private RunPlugins runPluginsOperator = new RunPlugins();
    
    public void setPluginData(PluginData data)
    {
        this.runPluginsOperator.setPluginData(data);
    }
    
    @Override
    public void execute() throws AlgorithmException
    {
        // On end launcher-plugins are executed
        runPluginsOperator.execute();
        
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, "Bye!");
    }
}

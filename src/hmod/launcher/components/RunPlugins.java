
package hmod.launcher.components;

import hmod.core.AlgorithmException;
import hmod.core.Operator;
import hmod.launcher.LauncherException;
import hmod.launcher.Launcher;
import hmod.launcher.Plugin;
import hmod.launcher.PluginInfo;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public class RunPlugins implements Operator
{
    private PluginData pluginData;
    private Plugin currentPlugin;
    
    public void setPluginData(PluginData handler)
    {
        this.pluginData = handler;
    }

    public Plugin getCurrentPlugin()
    {
        return currentPlugin;
    }

    @Override
    public void execute() throws AlgorithmException
    {
        Plugin[] plugins = pluginData.getPlugins();
        
        for(Plugin plugin : plugins)
        {
            currentPlugin = plugin;
            
            try
            {
                plugin.runPlugin();
            }
            catch(LauncherException ex)
            {
                PluginInfo info = plugin.getClass().getAnnotation(PluginInfo.class);
                String desc = (info == null ? "unnamed" : info.name() + " (" + info.version() + ")");

                OutputManager.println(Launcher.OUT_COMMON, "Plugin " + desc + " error: " + ex.getLocalizedMessage());
            }
        }
    }
}

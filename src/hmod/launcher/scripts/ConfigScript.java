
package hmod.launcher.scripts;

import static flexbuilders.basic.BasicBuilders.*;
import flexbuilders.core.BuildException;
import flexbuilders.scripting.BuildScript;
import flexbuilders.tree.TreeHandler;
import hmod.core.Config;

/**
 * Script that configures the general data of the <i>hMod</i> launcher.<p>
 * 
 * You may write or provide your own version of this class according to your 
 * implementation. Read the instructions below to learn how the file must be 
 * specified. This class, or any modified version of it, must be configured in 
 * the <i>'launcher.config.script'</i> entry of the <i>'hmod.properties'</i> 
 * file, in the application folder.
 * 
 * @author Enrique Urra C.
 */
public class ConfigScript extends BuildScript
{
    public ConfigScript(TreeHandler input)
    {
        super(input);
    }
    
    @Override
    public void process() throws BuildException
    {        
        Config cfg = Config.getInstance();
        
        String varDelimiter = cfg.getEntry("hmod.launcher.config.variableDelimiter");
        String batchPath = cfg.getEntry("hmod.launcher.config.batchPath");
        String outputPath = cfg.getEntry("hmod.launcher.config.outputPath");
        boolean debug = Boolean.parseBoolean(cfg.getEntry("hmod.launcher.config.debug"));
        boolean threading = Boolean.parseBoolean(cfg.getEntry("hmod.launcher.config.threading"));
        String defRunner = cfg.getEntry("hmod.launcher.config.defaultRunner");
        String defInterface = cfg.getEntry("hmod.launcher.config.defaultInterface");
        
        branch("consoleLauncher_varDelimiter").setBuildable(value(varDelimiter != null ? varDelimiter : "@"));
        
        branch("consoleLauncher_batchPath").setBuildable(value(batchPath != null ? batchPath : "batch"));
        
        branch("consoleLauncher_outputPath").setBuildable(value(outputPath != null ? outputPath : "output"));
        
        branch("consoleLauncher_debug").setBuildable(value(debug));
        
        branch("consoleLauncher_threading").setBuildable(value(threading));
        
        branch("consoleLauncher_defaultRunner").setBuildable(value(defRunner != null ? defRunner : "default"));        
        
        branch("consoleLauncher_defaultInterface").setBuildable(value(defInterface != null ? defInterface : "windowed"));
    
        // Default start step id. The string specified here will be used as 
        // the default id for the initial step of the generated algorithms. This
        // value can be further changed through the 'start_id' command.
        //branch("consoleLauncher_defaultStartId").setBuildable(value("start"));
    }
}

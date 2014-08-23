
package hmod.domains.launcher.core.ac;

import static flexbuilders.basic.BasicBuilders.*;
import flexbuilders.core.BuildException;
import flexbuilders.graph.BuilderGraph;
import hmod.core.HModConfig;
import static hmod.domains.launcher.core.Launcher.ENTRY_BATCH_PATH;
import static hmod.domains.launcher.core.Launcher.ENTRY_DEBUG;
import static hmod.domains.launcher.core.Launcher.ENTRY_INTERFACE_ID;
import static hmod.domains.launcher.core.Launcher.ENTRY_OUTPUT_PATH;
import static hmod.domains.launcher.core.Launcher.ENTRY_RUNNER_ID;
import static hmod.domains.launcher.core.Launcher.ENTRY_THREADING;
import static hmod.domains.launcher.core.Launcher.ENTRY_WINDOWS_AUTOCLOSE;

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
public final class ConfigLoader
{
    public void load(BuilderGraph graph) throws BuildException
    {       
        HModConfig cfg = HModConfig.getInstance();
        
        String batchPath = cfg.getEntry(ENTRY_BATCH_PATH, "batch");
        String outputPath = cfg.getEntry(ENTRY_OUTPUT_PATH, "output");
        boolean debug = Boolean.parseBoolean(cfg.getEntry(ENTRY_DEBUG, "false"));
        boolean threading = Boolean.parseBoolean(cfg.getEntry(ENTRY_THREADING, "false"));
        String defRunner = cfg.getEntry(ENTRY_RUNNER_ID, "default");
        String defInterface = cfg.getEntry(ENTRY_INTERFACE_ID, "windowed");
        boolean autoclose = Boolean.parseBoolean(cfg.getEntry(ENTRY_WINDOWS_AUTOCLOSE, "false"));
               
        graph.setValue(LauncherIds.BATCH_PATH_CONFIG, builderFor(batchPath != null ? batchPath : "batch"));        
        graph.setValue(LauncherIds.OUTPUT_PATH_CONFIG, builderFor(outputPath != null ? outputPath : "output"));        
        graph.setValue(LauncherIds.DEBUG_CONFIG, builderFor(debug));        
        graph.setValue(LauncherIds.THREADING_CONFIG, builderFor(threading));        
        graph.setValue(LauncherIds.RUNNER_ID_CONFIG, builderFor(defRunner != null ? defRunner : "default"));
        graph.setValue(LauncherIds.INTERFACE_ID_CONFIG, builderFor(defInterface != null ? defInterface : "windowed"));
        graph.setValue(LauncherIds.WINDOWS_AUTOCLOSE_CONFIG, builderFor(autoclose));
    }
}

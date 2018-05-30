
package hmod.launcher;

import hmod.core.AlgorithmException;
import hmod.core.Procedure;
import optefx.loader.ModuleLoader;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class Launcher
{
    public static final String OUT_COMMON = "launcher-output-common";
    public static final String OUT_ERROR = "launcher-output-error";
    
    private static final String ENTRY_SCRIPT_PATH = "hmod.launcher.batchPath";
    private static final String ENTRY_OUTPUT_PATH = "hmod.launcher.outputPath";
    private static final String ENTRY_DEBUG = "hmod.launcher.debug";
    private static final String ENTRY_THREADING = "hmod.launcher.threading";
    private static final String ENTRY_RUNNER_ID = "hmod.launcher.runnerId";
    private static final String ENTRY_INTERFACE_ID = "hmod.launcher.interfaceId";
    private static final String ENTRY_WINDOWS_AUTOCLOSE = "hmod.launcher.windowsAutoclose";
    private static final String ENTRY_BUNDLES_PATHS = "hmod.launcher.bundlesPaths";

    public static void main(String[] args) throws AlgorithmException
    {
        if(args.length == 0)
            runConsole();
        else
            runScript(args);
    }
    
    public static void runConsole() throws AlgorithmException, LauncherException
    {
        runScript(null);
    }
    
    public static void runScript(String[] args) throws AlgorithmException, LauncherException
    {
        init(args).run();
    }
    
    private static Procedure init(String[] args) throws AlgorithmException, LauncherException
    {
        OutputManager.getCurrent().setOutputsFromConfig(
            new OutputConfig().
            addSystemOutputId(OUT_COMMON).makePersistent(OUT_COMMON).
            addSystemErrorOutputId(OUT_ERROR).makePersistent(OUT_ERROR)
        );
        
        LauncherConfig cfg = LauncherConfig.getInstance();
        String bundlesPathsEntry = cfg.getEntry(ENTRY_BUNDLES_PATHS, "bundles");
        String[] paths = bundlesPathsEntry.split(";");
        
        ModuleLoader loader = new ModuleLoader().
            load(Console.class).
            setParameter(Console.SCRIPTS_PATH, cfg.getEntry(ENTRY_SCRIPT_PATH, "scripts")).
            setParameter(Console.OUTPUT_PATH, cfg.getEntry(ENTRY_OUTPUT_PATH, "output")).
            setParameter(Console.DEBUG, Boolean.parseBoolean(cfg.getEntry(ENTRY_DEBUG, "false"))).
            setParameter(Console.THREADING, Boolean.parseBoolean(cfg.getEntry(ENTRY_THREADING, "false"))).
            setParameter(Console.RUNNER_ID, cfg.getEntry(ENTRY_RUNNER_ID, "default")).
            setParameter(Console.INTERFACE_ID, cfg.getEntry(ENTRY_INTERFACE_ID, "windowed")).
            setParameter(Console.WINDOWS_AUTO_CLOSE, Boolean.parseBoolean(cfg.getEntry(ENTRY_WINDOWS_AUTOCLOSE, "false"))).
            setParameter(Console.BUNDLES_PATHS, paths);
        
        if(args != null)
        {
            StringBuilder sb = new StringBuilder(args[0]).append(" ").append("true");
            
            for(int i = 1; i < args.length; i++)
                sb.append(" ").append(args[i]);
            
            loader.setParameter(Console.INPUT_SCRIPT, sb.toString());
        }
        
        return loader.getInstance(Console.class).assemble();
    }
}

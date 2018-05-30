
package hmod.launcher;

import hmod.ap.FindOperations;
import hmod.ap.Operator;
import optefx.util.output.BasicManagerType;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
final class LauncherProcessing
{
    @Operator
    public static void initLauncher(LauncherControl lh, RandomSeed rh)
    {
        if(lh.isThreadingEnabled())
        {
            RandomTool.setMode(RandomTool.MODE_MULTI_THREAD);
            OutputManager.setCurrent(BasicManagerType.MULTI_THREAD);
        }
        else
        {
            RandomTool.setMode(RandomTool.MODE_SINGLE_THREAD);
            OutputManager.setCurrent(BasicManagerType.SINGLE_THREAD);
        }

        rh.setRandom();
    }
    
    @Operator
    public static void showInterface()
    {
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, "******************************");
        OutputManager.println(Launcher.OUT_COMMON, "**** hMod launcher console ***");
        OutputManager.println(Launcher.OUT_COMMON, "******************************");
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, "Type 'help' for view available commands");
    }
    
    @Operator
    public static void finishLauncher()
    {
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, "Bye!");
    }
}

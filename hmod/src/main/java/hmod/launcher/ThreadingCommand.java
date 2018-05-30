
package hmod.launcher;

import optefx.util.output.BasicManagerType;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 * Implements the 'threading' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="threading",
    usage="threading [set]",
    description="Switch the threading for the execution of algorithms. This"
        + " command affects directly the output and random mechanisms. Any"
        + " output configured will be cleared by calling this command.\n"
        + "[set]: 1 for enabling, 0 for disabling. Skip this argument to switch "
        + "the current value."
)
class ThreadingCommand extends Command
{
    private final LauncherHandler launcherHandler;

    public ThreadingCommand(LauncherHandler launcherHandler)
    {
        this.launcherHandler = launcherHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        boolean currState = launcherHandler.isThreadingEnabled();
        boolean toSet;
        
        if(args.getCount() < 1)
            toSet = !currState;
        else
            toSet = args.getArgAs(0, Boolean.class);
        
        if(!toSet)
        {
            launcherHandler.disableThreading();
            RandomTool.setMode(RandomTool.MODE_SINGLE_THREAD);
            OutputManager.setCurrent(BasicManagerType.SINGLE_THREAD);
            
            
            OutputManager.println(Launcher.OUT_COMMON, "Threading OFF.");
        }
        else
        {
            launcherHandler.enableThreading();
            RandomTool.setMode(RandomTool.MODE_MULTI_THREAD);
            OutputManager.setCurrent(BasicManagerType.MULTI_THREAD);
            
            OutputManager.println(Launcher.OUT_COMMON, "Threading ON.");
        }
    }
}
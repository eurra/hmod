
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.LauncherHandler;
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
public class ThreadingCommand extends Command
{
    private LauncherHandler launcherHandler;

    public void setLauncherHandler(LauncherHandler launcherHandler)
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
        else if(args.getString(0).equals("1"))
            toSet = true;
        else if(args.getString(0).equals("0"))
            toSet = false;
        else
            throw new CommandUsageException(this);
        
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
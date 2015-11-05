
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.Launcher;
import hmod.launcher.components.RunData;
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
    private RunData runHandler;

    public void setRunData(RunData handler)
    {
        this.runHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        boolean currState = runHandler.getThreading();
        boolean toSet;
        
        if(args.length < 1)
            toSet = !currState;
        else if(args[0].equals("1"))
            toSet = true;
        else if(args[0].equals("0"))
            toSet = false;
        else
            throw new LauncherException("Usage: " + getUsage());
        
        if(!toSet)
        {
            runHandler.setThreading(false);
            RandomTool.setMode(RandomTool.MODE_SINGLE_THREAD);
            OutputManager.setCurrent(BasicManagerType.SINGLE_THREAD);
            
            OutputManager.println(Launcher.OUT_COMMON, "Threading OFF.");
        }
        else
        {
            runHandler.setThreading(true);
            RandomTool.setMode(RandomTool.MODE_MULTI_THREAD);
            OutputManager.setCurrent(BasicManagerType.MULTI_THREAD);
            
            OutputManager.println(Launcher.OUT_COMMON, "Threading ON.");
        }
    }
}
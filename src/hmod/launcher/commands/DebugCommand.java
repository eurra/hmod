
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.Launcher;
import hmod.launcher.components.LauncherData;
import optefx.util.output.OutputManager;

/**
 * Implements the 'debug' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="debug",
    usage="debug [set]",
    description="Sets or switch the debugging state.\n"
        + "[set]: 1 for enabling, 0 for disabling. Skip this argument to switch "
        + "the current value."
)
public class DebugCommand extends Command
{
    private LauncherData launcherHandler;
    
    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        boolean currState = launcherHandler.getDebug();
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
            launcherHandler.setDebug(false);
            OutputManager.println(Launcher.OUT_COMMON, "Debugging OFF.");
        }
        else
        {
            launcherHandler.setDebug(true);
            OutputManager.println(Launcher.OUT_COMMON, "Debugging ON.");
        }
    }
}
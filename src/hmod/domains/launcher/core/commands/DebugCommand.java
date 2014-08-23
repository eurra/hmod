
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.LauncherHandler;
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
    private LauncherHandler launcherHandler;

    public void setLauncherHandler(LauncherHandler launcherHandler)
    {
        this.launcherHandler = launcherHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        boolean currState = launcherHandler.isDebugEnabled();
        boolean toSet;
        
        if(args.getCount() < 1)
        {
            toSet = !currState;
        }
        else
        {
            String val = args.getString(0);
            
            if(val.equals("1"))
                toSet = true;
            else if(val.equals("0"))
                toSet = false;
            else
                throw new CommandUsageException(this);
        }
        
        if(!toSet)
        {
            launcherHandler.disableDebugging();
            OutputManager.println(Launcher.OUT_COMMON, "Debugging OFF.");
        }
        else
        {
            launcherHandler.enableDebugging();
            OutputManager.println(Launcher.OUT_COMMON, "Debugging ON.");
        }
    }
}
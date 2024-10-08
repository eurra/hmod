
package hmod.launcher;

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
class DebugCommand extends Command
{
    private final LauncherControl launcherHandler;

    public DebugCommand(LauncherControl launcherHandler)
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
            boolean toEnable = args.getArgAs(0, Boolean.class);
            
            if(toEnable)
                toSet = true;
            else
                toSet = false;
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
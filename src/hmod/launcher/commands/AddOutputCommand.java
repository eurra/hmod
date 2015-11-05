
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.Command;
import hmod.launcher.CommandInfo;
import hmod.launcher.Launcher;
import hmod.launcher.components.LauncherData;
import optefx.util.output.OutputManager;


/**
 * Implements the 'add_console_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="add_output",
    usage="add_output <id>",
    description="Adds an algorithm system output handler.\n"
    + "<id>: the id to be associated with the handler."
)
public class AddOutputCommand extends Command
{
    private LauncherData launcherHandler;
    
    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        if(args.length < 1)
            throw new LauncherException("Usage: " + getUsage());
        
        launcherHandler.getOutputConfigBuilder().addSystemOutput(args[0]);
        OutputManager.println(Launcher.OUT_COMMON, "System output '" + args[0] + "' enabled.");
    }
}
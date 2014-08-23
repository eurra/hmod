
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.OutputConfigHandler;
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
    private OutputConfigHandler outputConfigHandler;

    public void setOutputConfigHandler(OutputConfigHandler outputConfigHandler)
    {
        this.outputConfigHandler = outputConfigHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
            throw new LauncherException("Usage: " + getInfo().usage());
        
        String output = args.getString(0);
        outputConfigHandler.addSystemOutput(output);
        OutputManager.println(Launcher.OUT_COMMON, "System output '" + output + "' enabled.");
    }
}
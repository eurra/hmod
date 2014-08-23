
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.OutputConfigHandler;
import optefx.util.output.OutputManager;

/**
 * Implements the 'clear_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="clear_outputs",
    usage="clear_outputs",
    description="Clears all the configured outputs."
)
public class ClearOutputsCommand extends Command
{
    private OutputConfigHandler outputConfigHandler;

    public void setOutputConfigHandler(OutputConfigHandler outputConfigHandler)
    {
        this.outputConfigHandler = outputConfigHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        outputConfigHandler.restartConfig();
        OutputManager.println(Launcher.OUT_COMMON, "All outputs cleared.");
    }
}
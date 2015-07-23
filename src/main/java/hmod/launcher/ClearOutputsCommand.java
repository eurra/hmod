
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 * Implements the 'clear_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="clearOutputs",
    usage="clearOutputs",
    description="Clears all the configured outputs."
)
class ClearOutputsCommand extends Command
{
    private final OutputConfigHandler outputConfigHandler;

    public ClearOutputsCommand(OutputConfigHandler outputConfigHandler)
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
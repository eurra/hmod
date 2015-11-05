
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.Launcher;
import hmod.launcher.components.LauncherData;
import optefx.util.output.DefaultOutputConfigBuilder;
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
    private LauncherData launcherHandler;
    
    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        launcherHandler.setOutputConfigBuilder(new DefaultOutputConfigBuilder());
        OutputManager.getCurrent().clearOutputs();
        OutputManager.println(Launcher.OUT_COMMON, "All outputs cleared.");
    }
}
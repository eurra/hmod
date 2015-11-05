
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.Launcher;
import hmod.launcher.components.LauncherData;
import optefx.util.output.OutputManager;

/**
 * Implements the 'restart' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="reset",
    usage="reset",
    description="Resets the currently loaded algorithm definitions."
)
public class ResetCommand extends Command
{
    private LauncherData launcherHandler;
    
    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        launcherHandler.getParser().restart();
        OutputManager.println(Launcher.OUT_COMMON, "Algorithm restarted.");
    }
}

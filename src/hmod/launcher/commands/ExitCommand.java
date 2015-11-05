
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.components.LauncherData;

/**
 * Implements the 'exit' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="exit",
    usage="exit",
    description="Exits from the execution console."
)
public class ExitCommand extends Command
{
    private LauncherData launcherHandler;
    
    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        launcherHandler.setRunning(false);
    }
}

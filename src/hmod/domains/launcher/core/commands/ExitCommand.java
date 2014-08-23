
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.ac.LauncherHandler;

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
    private LauncherHandler launcherHandler;

    public void setLauncherHandler(LauncherHandler launcherHandler)
    {
        this.launcherHandler = launcherHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        launcherHandler.stop();
    }
}

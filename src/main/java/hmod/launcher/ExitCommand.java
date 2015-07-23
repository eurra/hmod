
package hmod.launcher;

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
    private final LauncherHandler launcherHandler;

    public ExitCommand(LauncherHandler launcherHandler)
    {
        this.launcherHandler = launcherHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        launcherHandler.stop();
    }
}

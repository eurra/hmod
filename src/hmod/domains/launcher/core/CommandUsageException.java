
package hmod.domains.launcher.core;

/**
 *
 * @author Enrique Urra C.
 */
public class CommandUsageException extends LauncherException
{
    public CommandUsageException(Command command)
    {
        super("Usage: " + command.getInfo().usage());
    }
}

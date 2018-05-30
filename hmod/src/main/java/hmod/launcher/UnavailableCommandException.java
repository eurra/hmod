
package hmod.launcher;

/**
 * Implements a launcher exception for undefined commands.
 * @author Enrique Urra C.
 */
public class UnavailableCommandException extends LauncherException
{
    private String cmd;
    
    /**
     * Constructor, which provide the undefined command.
     * @param cmd The undefined command name.
     */
    public UnavailableCommandException(String cmd)
    {
        super("The specified command is not currently available: '" + cmd + "'");
        this.cmd = cmd;
    }

    public String getCmd()
    {
        return cmd;
    }
}


package hmod.launcher;

/**
 * Implements a launcher exception for undefined commands.
 * @author Enrique Urra C.
 */
public class UndefinedCommandException extends LauncherException
{
    private String cmd;
    
    /**
     * Constructor, which provide the undefined command.
     * @param cmd The undefined command name.
     */
    public UndefinedCommandException(String cmd)
    {
        super("The specified command is not supported: '" + cmd + "'");
        this.cmd = cmd;
    }

    public String getCmd()
    {
        return cmd;
    }
}

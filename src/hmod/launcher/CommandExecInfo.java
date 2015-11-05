
package hmod.launcher;

/**
 * Implements a bean which store the required data to execute a command.
 * @author Enrique Urra C.
 */
public class CommandExecInfo
{
    private Command cmd;
    private String args;

    public CommandExecInfo(Command cmd, String args)
    {
        this.cmd = cmd;
        this.args = args;
    }

    public Command getCmd()
    {
        return cmd;
    }

    public String getArgs()
    {
        return args;
    }
}

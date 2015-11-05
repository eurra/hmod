
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.Command;
import hmod.launcher.CommandInfo;
import hmod.launcher.CommandUsageException;
import hmod.launcher.Launcher;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="echo",
    usage="echo [<output_id>] <\"text\">",
    description="Prints text in the console.\n"
        + "[<output_id>]: the output id to use (console by default)."
        + "<\"text\">: the text to print."
)
public class EchoCommand extends Command
{
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        if(args.length < 1 || args.length > 2)
            throw new CommandUsageException(this);
        
        String outputId;
        String text;
        
        if(args.length == 1)
        {
            outputId = Launcher.OUT_COMMON;
            text = args[0];
        }
        else
        {
            outputId = args[0];
            text = args[1];
        }
        
        OutputManager.println(outputId, text);
    }
}

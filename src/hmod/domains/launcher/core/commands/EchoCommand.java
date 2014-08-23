
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
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
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1 || args.getCount() > 2)
            throw new CommandUsageException(this);
        
        String outputId;
        String text;
        
        if(args.getCount() == 1)
        {
            outputId = Launcher.OUT_COMMON;
            text = args.getString(0);
        }
        else
        {
            outputId = args.getString(0);
            text = args.getString(1);
        }
        
        OutputManager.println(outputId, text);
    }
}

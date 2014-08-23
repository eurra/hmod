
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.UndefinedCommandException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.CommandInfoHandler;
import optefx.util.output.OutputManager;

/**
 * Implements the 'help' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="help",
    usage="help [<cmd_name>]",
    description="Provides useful information regarding to the loaded commands.\n"
    + "<cmd_name> can be a single command to check."
)
public class HelpCommand extends Command
{
    private CommandInfoHandler commandInfoHandler;

    public void setCommandInfoHandler(CommandInfoHandler commandInfoHandler)
    {
        this.commandInfoHandler = commandInfoHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        String word = null;
        
        if(args.getCount() > 0)
            word = args.getString(0);
        
        if(word != null)
        {
            CommandInfo cmdInfo = null;
            
            try
            {
                cmdInfo = commandInfoHandler.getInfoForCommand(word);
            }
            catch(UndefinedCommandException ex)
            {
                throw new LauncherException(ex.getLocalizedMessage());
            }

            printCmd(cmdInfo);
        }
        else
        {
            CommandInfo[] allInfos = commandInfoHandler.getAllCommandInfos();

            for(CommandInfo cmd : allInfos)
                printCmd(cmd);
        }
    }
    
    private void printCmd(CommandInfo cmdInfo)
    {
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, cmdInfo.word().toUpperCase() + " (usage: '" + cmdInfo.usage()+ "')");
        OutputManager.println(Launcher.OUT_COMMON, cmdInfo.description());
    }
}
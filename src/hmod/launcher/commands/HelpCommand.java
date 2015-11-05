
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.UndefinedCommandException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.CommandRegister;
import hmod.launcher.Launcher;
import hmod.launcher.components.CommandData;
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
    private CommandData commandHandler;

    public void setCommandData(CommandData handler)
    {
        this.commandHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        String word = null;
        
        if(args.length > 0)
            word = args[0];
        
        CommandRegister register = commandHandler.getCommandRegister();
        
        if(word != null)
        {
            Command cmd = null;
            
            try
            {
                cmd = register.checkCommand(word);
            }
            catch(UndefinedCommandException ex)
            {
                throw new LauncherException(ex.getLocalizedMessage());
            }

            printCmd(cmd);
        }
        else
        {
            Command[] cmds = register.getAllCommands();

            for(Command cmd : cmds)
                printCmd(cmd);
        }
    }
    
    private void printCmd(Command cmd)
    {
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, cmd.getWord().toUpperCase() + " (usage: '" + cmd.getUsage() + "')");
        OutputManager.println(Launcher.OUT_COMMON, cmd.getDescription());
    }
}
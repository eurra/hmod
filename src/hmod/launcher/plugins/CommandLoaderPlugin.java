
package hmod.launcher.plugins;

import hmod.launcher.LauncherException;
import hmod.launcher.Command;
import hmod.launcher.CommandBufferManager;
import hmod.launcher.CommandParser;
import hmod.launcher.CommandRegister;
import hmod.launcher.Launcher;
import hmod.launcher.Plugin;
import hmod.launcher.components.CommandData;
import hmod.launcher.components.VariableData;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public class CommandLoaderPlugin implements Plugin
{
    private CommandData commandHandler;
    private VariableData variableHandler;

    public void setCommandData(CommandData handler)
    {
        this.commandHandler = handler;
    }

    public void setVariableData(VariableData handler)
    {
        this.variableHandler = handler;
    }
    
    @Override
    public void runPlugin() throws LauncherException
    {        
        CommandRegister register = new CommandRegister();        
        commandHandler.setCommandRegister(register);
        
        CommandBufferManager buffer = new CommandBufferManager();
        commandHandler.setCommandBufferManager(buffer);
        
        CommandParser parser = new CommandParser(register, variableHandler.getVariableManager());
        commandHandler.setCommandParser(parser);
        
        OutputManager.print(Launcher.OUT_COMMON, "Loading commands... ");
        loadCmds(register, commandHandler.getCommands());
        
        OutputManager.println(Launcher.OUT_COMMON, "done.");
    }
    
    private void loadCmds(CommandRegister register, Command[] cmds)
    {
        Command currCmd = null;
        
        for(Command cmd : cmds)
        {
            currCmd = cmd;
        
            try
            {
                register.registerCommand(cmd);
            }
            catch(LauncherException ex)
            {
                OutputManager.print(Launcher.OUT_COMMON, "\nError loading command '" + currCmd.getClass().getCanonicalName() + "': " + ex.getMessage() + "\n");
            }
        }        
    }
}

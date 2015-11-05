
package hmod.launcher.components;

import hmod.core.AlgorithmException;
import hmod.core.BooleanOperator;
import hmod.launcher.UndefinedCommandException;
import hmod.launcher.CommandExecInfo;
import hmod.launcher.CommandParser;
import hmod.launcher.Launcher;
import optefx.util.output.OutputManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Enrique Urra C.
 */
public class ReadNextCommand extends BooleanOperator
{
    private CommandData commandHandler;
    private BufferedReader reader;
    private RunPlugins runPluginsOperator = new RunPlugins();
    
    public void setCommandData(CommandData handler)
    {
        this.commandHandler = handler;
    }
    
    public void setPluginData(PluginData data)
    {
        this.runPluginsOperator.setPluginData(data);
    }

    @Override
    public Boolean evaluate() throws AlgorithmException
    {
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.print(Launcher.OUT_COMMON, "> ");
        
        CommandParser parser = commandHandler.getCommandParser();
        
        if(reader == null)
            reader = new BufferedReader(new InputStreamReader(System.in));
        
        CommandExecInfo cmdInfo = null;
        
        try
        {
            cmdInfo = parser.parseInput(reader.readLine());
        }
        catch(IOException ex)
        {
            throw new AlgorithmException(ex);
        }
        catch(UndefinedCommandException ex)
        {
            OutputManager.println(Launcher.OUT_COMMON, ex.getLocalizedMessage());
            return false;
        }
        
        if(cmdInfo == null)
            return false;
        
        commandHandler.setCurrentCommand(cmdInfo);
        
        // On command loaded-plugins are executed
        runPluginsOperator.execute();
        return true;
    }    
}

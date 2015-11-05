
package hmod.launcher.commands;

import hmod.parser.AlgorithmParser;
import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.CommandUsageException;
import hmod.launcher.Launcher;
import hmod.launcher.components.LauncherData;
import hmod.parser.AlgorithmParserException;
import optefx.util.output.OutputManager;

/**
 * Implements the 'load' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="load",
    usage="load <input> [<arg1>, <arg2>, ..., <argN>]",
    description="Load an algorithm input.\n"
    + " <input>: The location of the script, a valid class (with package) name.\n"
    + " [<arg1>, <arg2>, ..., <argN>]: String arguments passed to the input"
)
public class LoadCommand extends Command
{
    private LauncherData launcherHandler;
    
    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        if(args.length < 1)
            throw new CommandUsageException(this);
        
        String location = args[0];
        String[] inputArgs = new String[args.length - 1];
        
        for(int i = 1; i < args.length; i++)
            inputArgs[i - 1] = args[i];
        
        AlgorithmParser parser = launcherHandler.getParser();
        
        try
        {
            parser.load(location, (Object[])inputArgs);
        }
        catch(AlgorithmParserException ex)
        {
            if(ex.requireRestart())
                parser.restart();
            
            throw new LauncherException(ex.getMessage(), ex);
        }

        OutputManager.println(Launcher.OUT_COMMON, "Load successful (" + location + ").");
    }
}
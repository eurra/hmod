
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 * Implements the 'load' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="load",
    usage="load <input> [args...]",
    description="Loads an algorithm input.\n"
    + " <input>: The input to load.\n"
    + " [args...]: Arguments passed to the input"
)
public class LoadCommand extends Command
{
    private final AlgorithmLoaderHandler algorithmParserHandler;

    public LoadCommand(AlgorithmLoaderHandler algorithmParserHandler)
    {
        this.algorithmParserHandler = algorithmParserHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        int argsCount = args.getCount();
        
        if(argsCount < 2)
            throw new CommandUsageException(this);
        
        AlgorithmLoader loader = algorithmParserHandler.getCurrentLoader();
        String input = args.getString(0);
        
        Object[] additionalArgs = new Object[argsCount - 1];
        
        for(int i = 1; i < argsCount; i++)
            additionalArgs[i - 1] = args.getObject(i);
        
        try
        {
            loader.load(input, additionalArgs);
        }
        catch(AlgorithmLoadException ex)
        {
            if(ex.requireRestart())
                loader.restart();
            
            throw new LauncherException(ex);
        }
        catch(Throwable ex)
        {
            throw new LauncherException(ex);
        }

        OutputManager.println(Launcher.OUT_COMMON, "Load successful (" + args.getObject(0) + ").");
    }
}
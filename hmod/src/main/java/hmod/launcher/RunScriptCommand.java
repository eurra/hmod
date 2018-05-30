
package hmod.launcher;

import optefx.util.output.OutputManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.script.ScriptException;

/**
 * Implements the 'batch' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="runScript",
    usage="runScript <input> [reset] [arg1, arg2, ..., argN]",
    description="Runs a set of scripted commands from a file in packages or "
        + "the filesystem.\n"
    + "<input>: the input from which the commands will be extracted. The "
        + "filesystem will be searched first, packages after.\n"
    + "[reset]: a boolean that indicates if the script environment will be reset "
        + "before execution (true by default).\n"
    + "[argX]: Additional arguments passed to the scripts. They will be stored in "
        + "String format within an array variable named 'scriptArgs'."
)
class RunScriptCommand extends Command
{
    private final LauncherHandler launcherHandler;
    private final ScriptEngineHandler scriptEngineHandler;

    public RunScriptCommand(LauncherHandler launcherHandler, 
                            ScriptEngineHandler scriptEngineHandler)
    {
        this.launcherHandler = launcherHandler;
        this.scriptEngineHandler = scriptEngineHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        int argsCount = args.getCount();
        
        if(argsCount < 1)
            throw new LauncherException("Usage: " + getInfo().usage());
        
        String input = args.getString(0);        
        BufferedReader reader = null;
        
        try
        {
            reader = new BufferedReader(new FileReader(input));
        }
        catch(FileNotFoundException ex)
        {
            String batchPath = launcherHandler.getScriptBaseFolder();
            File inputPath = new File(batchPath, input);
            
            try
            {
                reader = new BufferedReader(new FileReader(inputPath));
            }
            catch(FileNotFoundException ex2)
            {
                InputStream is = RunScriptCommand.class.getResourceAsStream(input);
        
                if(is != null)
                    reader = new BufferedReader(new InputStreamReader(is));
            }
        }
        
        if(reader == null)
            throw new LauncherException("The provided input (" + input + ") was not found");
                
        if(argsCount >= 1 || args.getArgAs(1, Boolean.class))
        {
            scriptEngineHandler.restart();
            OutputManager.println(Launcher.OUT_COMMON, "Script engine reset.");
        }
        
        if(argsCount > 1)
        {
            Object[] scriptArgs = new Object[argsCount - 2];
            
            for(int i = 2; i < argsCount; i++)
                scriptArgs[i - 2] = args.getObject(i);
            
            scriptEngineHandler.setVariable("scriptArgs", scriptArgs);
        }
        else
        {
            scriptEngineHandler.setVariable("scriptArgs", new Object[0]);
        }
        
        OutputManager.println(Launcher.OUT_COMMON, "*** Running script (" + input + ")...");
        
        try
        {
            scriptEngineHandler.eval(reader, input);
        }
        catch(ScriptException ex)
        {
            throw new LauncherException("Script exception: " + ex.getLocalizedMessage(), ex);
        }
        catch(Throwable trw)
        {
            throw new LauncherException("Error when executing script '" + input + "': '" + trw.getLocalizedMessage() + "'", trw);
        }
        
        OutputManager.println(Launcher.OUT_COMMON, "*** Script complete.");
    }
}
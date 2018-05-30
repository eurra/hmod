
package hmod.launcher;

import hmod.ap.FindOperations;
import hmod.ap.Operator;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import jline.console.ConsoleReader;
import jline.console.history.FileHistory;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
class LauncherInput
{
    private LauncherControl lh; 
    private CommandParser cph;
    private ConsoleReader reader;
    private FileHistory history;

    public LauncherInput(LauncherControl lh, 
                         CommandParser cph,
                         CommandRegistry cr)
    {
        this.lh = lh;
        this.cph = cph;
        
        try
        {
            reader = new ConsoleReader();
            File hFile = new File(".history");
            hFile.createNewFile();
            history = new FileHistory(hFile);
            reader.setHistory(history);
            reader.setHistoryEnabled(true);  
            reader.addCompleter(cr.getCompleter());
            reader.setPrompt("\nhmod> ");
        }
        catch(IOException ex)
        {
            throw new LauncherException(ex);
        }
    }
    
    private void readInput(String input)
    {
        CommandRunner runner = null;

        try
        {
            runner = cph.parseCommand(input);
        }
        catch(UndefinedCommandException ex)
        {
            OutputManager.println(Launcher.OUT_COMMON, ex.getLocalizedMessage());
        }

        if(runner != null)
        {
            boolean debug = lh.isDebugEnabled();

            try
            {
                runner.runCommand();
            }
            catch(CommandUsageException ex)
            {
                OutputManager.println(Launcher.OUT_ERROR, ex.getMessage());
            }
            catch(LauncherException ex)
            {
                if(debug)
                {
                    ex.printStackTrace(OutputManager.getCurrent().getOutput(Launcher.OUT_ERROR));
                }
                else
                {
                    OutputManager.println(Launcher.OUT_ERROR, ex.getMessage());
                    OutputManager.println(Launcher.OUT_COMMON, "The command execution has been terminated.");
                }
            }
            catch(RuntimeException ex)
            {
                if(debug)
                    ex.printStackTrace(OutputManager.getCurrent().getOutput(Launcher.OUT_ERROR));
                else
                    OutputManager.println(Launcher.OUT_ERROR, getErrorDetail(ex));
            }
        }
    }
    
    private String getErrorDetail(Exception ex)
    {
        StackTraceElement[] stackTrace = ex.getStackTrace();        
        return "Error (" + ex.toString() + "): '" + ex.getMessage() + "', " + stackTrace[0].toString();
    }
    
    @Operator
    public void readConsoleCommand()
    {
        try
        {
            readInput(reader.readLine());
            history.flush();
        }
        catch(IOException ex)
        {
            throw new LauncherException(ex);
        }
    }
    
    @Operator
    public void readScriptCommand(String script)
    {
        readInput(script);
    }
}

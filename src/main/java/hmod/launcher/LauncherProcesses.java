
package hmod.launcher;

import hmod.core.Statement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import optefx.util.output.BasicManagerType;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
final class LauncherProcesses
{
    private final LauncherHandler lh;
    private final RandomHandler rh;
    private final CommandParseHandler cph;

    public LauncherProcesses(LauncherHandler lh, 
                             RandomHandler rh, 
                             CommandParseHandler cph)
    {
        this.lh = lh;
        this.rh = rh;
        this.cph = cph;
    }
    
    public void initLauncher()
    {
        if(lh.isThreadingEnabled())
        {
            RandomTool.setMode(RandomTool.MODE_MULTI_THREAD);
            OutputManager.setCurrent(BasicManagerType.MULTI_THREAD);
        }
        else
        {
            RandomTool.setMode(RandomTool.MODE_SINGLE_THREAD);
            OutputManager.setCurrent(BasicManagerType.SINGLE_THREAD);
        }

        rh.setRandomSeed();
    }
    
    public void showInterface()
    {
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, "******************************");
        OutputManager.println(Launcher.OUT_COMMON, "**** hMod launcher console ***");
        OutputManager.println(Launcher.OUT_COMMON, "******************************");
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, "Type 'help' for view available commands");
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
    
    private static String getErrorDetail(Exception ex)
    {
        StackTraceElement[] stackTrace = ex.getStackTrace();        
        return "Error (" + ex.toString() + "): '" + ex.getMessage() + "', " + stackTrace[0].toString();
    }
    
    public void readConsoleCommand()
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.print(Launcher.OUT_COMMON, "> ");

        try
        {
            readInput(reader.readLine());
        }
        catch(IOException ex)
        {
            throw new LauncherException(ex);
        }
    }
    
    public Statement readScriptCommand(String script)
    {
        return () -> readInput(script);
    }
    
    public void finishLauncher()
    {
        OutputManager.println(Launcher.OUT_COMMON);
        OutputManager.println(Launcher.OUT_COMMON, "Bye!");
    }
}

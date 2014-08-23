
package hmod.domains.launcher.core.ac;

import hmod.core.AlgorithmException;
import hmod.core.Condition;
import hmod.core.Operator;
import hmod.domains.launcher.core.CommandRunner;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.UndefinedCommandException;
import hmod.loader.graph.ArgumentFactory;
import hmod.loader.graph.ArgumentList;
import hmod.loader.graph.ArgumentInfo;
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
public final class LauncherOperatorFactory
{
    /**
     * Operator for initializing the launcher.
     * @param list The argument list.
     */
    @ArgumentInfo(type = LauncherHandler.class)
    @ArgumentInfo(type = RandomHandler.class)
    public Operator initLauncher(ArgumentList list)
    {
        LauncherHandler launcherHandler = list.next(LauncherHandler.class);
        RandomHandler randomHandler = list.next(RandomHandler.class);
                
        return () -> {
            if(launcherHandler.isThreadingEnabled())
            {
                RandomTool.setMode(RandomTool.MODE_MULTI_THREAD);
                OutputManager.setCurrent(BasicManagerType.MULTI_THREAD);
            }
            else
            {
                RandomTool.setMode(RandomTool.MODE_SINGLE_THREAD);
                OutputManager.setCurrent(BasicManagerType.SINGLE_THREAD);
            }

            randomHandler.setRandomSeed();

            OutputManager.println(Launcher.OUT_COMMON);
            OutputManager.println(Launcher.OUT_COMMON, "******************************");
            OutputManager.println(Launcher.OUT_COMMON, "**** hMod launcher console ***");
            OutputManager.println(Launcher.OUT_COMMON, "******************************");
            OutputManager.println(Launcher.OUT_COMMON);
            OutputManager.println(Launcher.OUT_COMMON, "Type 'help' for view available commands");  
        };
    }

    @ArgumentInfo(type = CommandParseHandler.class)
    public Condition readNextCommand(ArgumentList list)
    {
        CommandParseHandler commandParseHandler = list.next(CommandParseHandler.class);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        return () -> {
            OutputManager.println(Launcher.OUT_COMMON);
            OutputManager.print(Launcher.OUT_COMMON, "> ");

            CommandRunner runner = null;

            try
            {
                runner = commandParseHandler.parseNextCommand(reader.readLine());
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

            return runner != null;
        };
    }
    
    private String getErrorDetail(Exception ex)
    {
        StackTraceElement[] stackTrace = ex.getStackTrace();        
        return "Error (" + ex.toString() + "): '" + ex.getMessage() + "', " + stackTrace[0].toString();
    }
    
    @ArgumentInfo(type = LauncherHandler.class)
    @ArgumentInfo(type = CommandParseHandler.class)
    public Operator executeCurrentCommand(ArgumentList list)
    {
        LauncherHandler launcherHandler = list.next(LauncherHandler.class);
        CommandParseHandler commandParseHandler = list.next(CommandParseHandler.class);
        
        return () -> {
            CommandRunner cmdRunner = commandParseHandler.getCommandCurrentlyParsed();
            boolean debug = launcherHandler.isDebugEnabled();

            try
            {
                cmdRunner.runCommand();
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
        };
    }
    
    @ArgumentFactory
    public Operator finishLauncher(ArgumentList list)
    {
        return () -> {
            OutputManager.println(Launcher.OUT_COMMON);
            OutputManager.println(Launcher.OUT_COMMON, "Bye!");
        };
    }
    
    @ArgumentInfo(type = LauncherHandler.class)
    public Condition checkLauncherFinished(ArgumentList list)
    {
        LauncherHandler launcherHandler = list.next(LauncherHandler.class);
        
        return () -> {
            return !launcherHandler.isRunning();
        };
    }
}

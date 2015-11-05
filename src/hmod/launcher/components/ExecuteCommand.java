
package hmod.launcher.components;

import hmod.core.AlgorithmException;
import hmod.core.Operator;
import hmod.launcher.LauncherException;
import hmod.launcher.Command;
import hmod.launcher.CommandExecInfo;
import hmod.launcher.CommandUsageException;
import hmod.launcher.Launcher;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public class ExecuteCommand implements Operator
{
    private LauncherData launcherData;
    private CommandData commandData;
    private RunPlugins runPluginsOperator = new RunPlugins();

    public void setLauncherData(LauncherData launcherData)
    {
        this.launcherData = launcherData;
    }
    
    public void setCommandData(CommandData handler)
    {
        this.commandData = handler;
    }
    
    public void setPluginData(PluginData data)
    {
        this.runPluginsOperator.setPluginData(data);
    }
    
    @Override
    public void execute() throws AlgorithmException
    {
        CommandExecInfo cmdInfo = commandData.getCurrentCommand();
        Command cmd = cmdInfo.getCmd();
        String args = cmdInfo.getArgs();
        boolean debug = launcherData.getDebug();
        
        try
        {
            cmd.executeCommand(commandData.getCommandParser().parseArguments(args));
        }
        catch(CommandUsageException ex)
        {
            OutputManager.println(Launcher.OUT_ERROR, ex.getMessage());
        }
        catch(LauncherException ex)
        {
            if(debug)
            {
                ex.printStackTrace(OutputManager.getCurrent().getOutput(Launcher.OUT_COMMON));
            }
            else
            {
                OutputManager.println(Launcher.OUT_ERROR, ex.getMessage());
                OutputManager.println(Launcher.OUT_COMMON, "The command execution has been terminated.");
            }
        }
        catch(Exception ex)
        {
            if(debug)
                ex.printStackTrace(OutputManager.getCurrent().getOutput(Launcher.OUT_COMMON));
            else
                OutputManager.println(Launcher.OUT_ERROR, getErrorDetail(ex));
        }
        
        // On command executed-plugins are executed
        runPluginsOperator.execute();
    }
    
    private String getErrorDetail(Exception ex)
    {
        StackTraceElement[] stackTrace = ex.getStackTrace();        
        return "Error (" + ex.toString() + "): '" + ex.getMessage() + "', " + stackTrace[0].toString();
    }
}

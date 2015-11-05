
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.Launcher;
import hmod.launcher.components.WindowsData;
import optefx.util.output.OutputManager;

/**
 * Implements the 'add_file_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="windows_cfg",
    usage="windows_cfg [aclose]",
    description="Configures the algorithm windowed mode.\n"
    + "[aclose]: enables (1) or disables (0) the windows auto-close, in which an"
        + " active window is automatically closed when the related algorithm "
        + "finishes."
)
public class WindowsConfigCommand extends Command
{
    private WindowsData windowsHandler;

    public void setWindowsData(WindowsData outputModeHandler)
    {
        this.windowsHandler = outputModeHandler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        if(args.length < 1)
            throw new LauncherException("Usage: " + getUsage());
        
        boolean aClose;
        
        switch(args[0])
        {
            case "1":
                aClose = true;
                break;
            case "0":
                aClose = false;
                break;
            default:
                throw new LauncherException("Usage: " + getUsage());
        }
        
        windowsHandler.setWindowAutoClose(aClose);
        OutputManager.println(Launcher.OUT_COMMON, "Windowed config: auto-close=" + (aClose ? "ON" : "OFF") + ".");
    }    
}
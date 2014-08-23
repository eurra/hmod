
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.WindowsHandler;
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
    private WindowsHandler windowsHandler;

    public void setWindowsHandler(WindowsHandler windowsHandler)
    {
        this.windowsHandler = windowsHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
            throw new LauncherException("Usage: " + getInfo().usage());
        
        boolean aClose;
        
        switch(args.getString(0))
        {
            case "1":
                aClose = true;
                break;
            case "0":
                aClose = false;
                break;
            default:
                throw new LauncherException("Usage: " + getInfo().usage());
        }
        
        if(aClose)
            windowsHandler.enableAutoClose();
        else
            windowsHandler.disableAutoClose();
        
        OutputManager.println(Launcher.OUT_COMMON, "Windowed config: auto-close=" + (aClose ? "ON" : "OFF") + ".");
    }    
}
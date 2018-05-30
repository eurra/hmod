
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 * Implements the 'add_file_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="windowsCfg",
    usage="windowsCfg [aclose]",
    description="Configures the algorithm windowed mode.\n"
    + "[aclose]: enables (true) or disables (false) the windows auto-close, in which an"
        + " active window is automatically closed when the related algorithm "
        + "finishes."
)
class WindowsConfigCommand extends Command
{
    private final WindowsSettings windowsHandler;

    public WindowsConfigCommand(WindowsSettings windowsHandler)
    {
        this.windowsHandler = windowsHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
            throw new LauncherException("Usage: " + getInfo().usage());
        
        boolean aClose;
        
        if(args.getArgAs(0, Boolean.class))
            aClose = true;
        else
            aClose = false;
        
        if(aClose)
            windowsHandler.enableAutoClose();
        else
            windowsHandler.disableAutoClose();
        
        OutputManager.println(Launcher.OUT_COMMON, "Windowed config: auto-close=" + (aClose ? "ON" : "OFF") + ".");
    }    
}
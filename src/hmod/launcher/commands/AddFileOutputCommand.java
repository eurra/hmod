
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.Launcher;
import hmod.launcher.components.LauncherData;
import optefx.util.output.OutputManager;

/**
 * Implements the 'add_file_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="add_file_output",
    usage="add_file_output <id> <file_path> [<append>]",
    description="Adds a algorithm file output handler.\n"
    + "<id>: the id to be associated with the handler.\n"
    + "<file_path>: the file for output.\n"
    + "<append>: option to append, 1 for enable, 0 for disable."
)
public class AddFileOutputCommand extends Command
{
    private LauncherData launcherHandler;
    
    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        if(args.length < 2)
            throw new LauncherException("Usage: " + getUsage());
        
        int appendInt = 0;
        
        if(args.length >= 3)
        {
            try
            {
                appendInt = Integer.parseInt(args[2]);
            }
            catch(NumberFormatException ex)
            {
                throw new LauncherException("Usage: " + getUsage(), ex);
            }

            if(appendInt != 0 && appendInt != 1)
                throw new LauncherException("Usage: " + getUsage());
        }
        
        String id = args[0];
        String filePath = args[1];
        boolean append = appendInt == 0 ? false : true;   
        launcherHandler.getOutputConfigBuilder().addFileOutput(id, filePath, append);
        OutputManager.println(Launcher.OUT_COMMON, "File '" + filePath + "' (append=" + (append ? "ON" : "OFF") +") added to the output '" + id + "'.");
    }    
}
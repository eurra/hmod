
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.OutputConfigHandler;
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
    private OutputConfigHandler outputConfigHandler;

    public void setOutputConfigHandler(OutputConfigHandler outputConfigHandler)
    {
        this.outputConfigHandler = outputConfigHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 2)
            throw new CommandUsageException(this);
        
        int appendInt = 0;
        
        if(args.getCount() >= 3)
        {
            try
            {
                appendInt = Integer.parseInt(args.getString(2));
            }
            catch(NumberFormatException ex)
            {
                throw new LauncherException("Usage: " + getInfo().usage(), ex);
            }

            if(appendInt != 0 && appendInt != 1)
                throw new LauncherException("Usage: " + getInfo().usage());
        }
        
        String id = args.getString(0);
        String filePath = args.getString(1);
        boolean append = appendInt != 0;   
        outputConfigHandler.addFileOutput(id, filePath, append);
        OutputManager.println(Launcher.OUT_COMMON, "File '" + filePath + "' (append=" + (append ? "ON" : "OFF") +") added to the output '" + id + "'.");
    }    
}
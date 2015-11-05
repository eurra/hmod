
package hmod.launcher.commands;

import hmod.launcher.CommandBufferManager;
import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.CommandBuffer;
import hmod.launcher.Launcher;
import hmod.launcher.components.CommandData;
import optefx.util.output.OutputManager;

/**
 * Implements the 'run' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="repeat",
    usage="repeat <times> [\"description\"]",
    description="Repeats the following command set a number of times until an 'end_repeat' "
        + "command is found.\n"
        + "<times>: the repeat count to use.\n"
        + "[\"description\"]: an optional description for the repeating."
)
public class RepeatCommand extends Command
{
    private CommandData commandHandler;

    public void setCommandData(CommandData handler)
    {
        this.commandHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {     
        CommandBufferManager bufferManager = commandHandler.getCommandBufferManager();
                
        if(!bufferManager.existsBuffer())
            throw new LauncherException("Repetition cannot be applied in the current context.");
        
        if(args.length < 1)
            throw new LauncherException("Usage: " + getUsage());
        
        int counts = -1;
        String desc = args.length > 1 ? args[1] : null;
        
        try
        {
            counts = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException ex)
        {
            throw new LauncherException(ex);
        }
        
        CommandBuffer buffer = bufferManager.getBuffer();
        buffer.pushMark(counts, desc);
        OutputManager.println(Launcher.OUT_COMMON, "*** Starting repetition" + (desc == null ? "" : " '" + desc + "'") + " (" + (buffer.getMarkResetLimit() - buffer.getMarkResetCount()) + " remaining)...");
    }
}
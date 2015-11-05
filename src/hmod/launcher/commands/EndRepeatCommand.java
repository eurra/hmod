
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
    word="end_repeat",
    usage="end_repeat",
    description="Finish a repeating iteration, restarting from the last repeat "
        + "position, if there are remaining iterations."
)
public class EndRepeatCommand extends Command
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
        
        CommandBuffer buffer = bufferManager.getBuffer();
        
        if(!buffer.isMarked())
            throw new LauncherException("No repetitions have been previously started.");
        
        String desc = buffer.getMarkDescription();
        
        if(!buffer.reset())
        {
            OutputManager.println(Launcher.OUT_COMMON, "*** Repetition" + (desc == null ? "" : " '" + desc + "'") + " finished.");
            buffer.popMark();
        }
        else
        {
            OutputManager.println(Launcher.OUT_COMMON, "*** Next repetition" + (desc == null ? "" : " '" + desc + "'") + " (" + (buffer.getMarkResetLimit() - buffer.getMarkResetCount()) +" remaining)...");
        }
    }
}
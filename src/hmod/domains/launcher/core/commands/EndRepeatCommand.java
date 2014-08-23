
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.BatchEnvironmentCommand;
import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandBuffer;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.CommandBufferHandler;
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
public class EndRepeatCommand extends BatchEnvironmentCommand
{
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        CommandBuffer buffer = getBufferHandler().getCurrentBuffer();
        
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

package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.BatchEnvironmentCommand;
import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandBuffer;
import hmod.domains.launcher.core.Launcher;
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
public class RepeatCommand extends BatchEnvironmentCommand
{    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
            throw new LauncherException("Usage: " + getInfo().usage());
        
        int counts = -1;
        String desc = args.getCount() > 1 ? args.getString(1) : null;
        
        try
        {
            counts = Integer.parseInt(args.getString(0));
        }
        catch(NumberFormatException ex)
        {
            throw new LauncherException(ex);
        }
        
        CommandBuffer buffer = getBufferHandler().getCurrentBuffer();
        buffer.pushMark(counts, desc);
        OutputManager.println(Launcher.OUT_COMMON, "*** Starting repetition" + (desc == null ? "" : " '" + desc + "'") + " (" + (buffer.getMarkResetLimit() - buffer.getMarkResetCount()) + " remaining)...");
    }
}
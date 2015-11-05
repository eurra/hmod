
package hmod.launcher.varproc;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandBuffer;
import hmod.launcher.VariableProcessor;
import hmod.launcher.components.CommandData;

/**
 *
 * @author Enrique Urra C.
 */
public class BatchEnvProcessor implements VariableProcessor
{
    private CommandData commandHandler;
    
    public void setCommandData(CommandData handler)
    {
        this.commandHandler = handler;
    }
    
    @Override
    public String process(String input) throws LauncherException
    {
        CommandBuffer buffer = commandHandler.getCommandBufferManager().getBuffer();
        boolean noBuffer = buffer == null || !buffer.isMarked();
        
        String currentRep, maxRep, remainingReps;
        
        if(noBuffer)
        {
            currentRep = maxRep = remainingReps = "-";
        }
        else
        {
            maxRep = Integer.toString(buffer.getMarkResetLimit());
            
            if(maxRep.length() > 1)
            {
                String format = "%0" + maxRep.length() + "d";
                currentRep = String.format(format, buffer.getMarkResetCount());        
                remainingReps = String.format(format, buffer.getMarkResetLimit() - buffer.getMarkResetCount());
            }
            else
            {
                currentRep = Integer.toString(buffer.getMarkResetCount());        
                remainingReps = Integer.toString(buffer.getMarkResetLimit() - buffer.getMarkResetCount());
            }
        }
        
        String res = input.replace("CURR_REP", currentRep)
            .replace("MAX_REP", maxRep)
            .replace("REM_REPS", remainingReps);
        
        return res;
    }
}

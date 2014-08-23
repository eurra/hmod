
package hmod.domains.launcher.core.textproc;

import hmod.domains.launcher.core.CommandBuffer;
import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.TextVariableProcessor;
import hmod.domains.launcher.core.ac.CommandBufferHandler;

/**
 *
 * @author Enrique Urra C.
 */
public class BatchEnvironmentProcessor implements TextVariableProcessor
{
    private CommandBufferHandler commandBufferHandler;

    public void setCommandBufferHandler(CommandBufferHandler commandBufferHandler)
    {
        this.commandBufferHandler = commandBufferHandler;
    }
    
    @Override
    public String process(String input) throws LauncherException
    {
        if(!input.equals("CURR_REP") && !input.equals("MAX_REP") && !input.equals("REM_REPS"))
            return null;
        
        CommandBuffer buffer = commandBufferHandler.getCurrentBuffer();
        
        if(buffer == null || !buffer.isMarked())
            return "-";
        
        String maxRep = Integer.toString(buffer.getMarkResetLimit());
        
        if(input.equals("MAX_REP"))
            return maxRep;
        
        String format = "%0" + maxRep.length() + "d";
        
        if(input.equals("CURR_REP"))
            return String.format(format, buffer.getMarkResetCount());
        else
            return String.format(format, buffer.getMarkResetLimit() - buffer.getMarkResetCount());
    }
}

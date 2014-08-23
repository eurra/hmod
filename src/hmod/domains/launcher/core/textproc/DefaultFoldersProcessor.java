
package hmod.domains.launcher.core.textproc;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.TextVariableProcessor;
import hmod.domains.launcher.core.ac.LauncherHandler;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultFoldersProcessor implements TextVariableProcessor
{
    private LauncherHandler launcherHandler;

    public void setLauncherHandler(LauncherHandler launcherHandler)
    {
        this.launcherHandler = launcherHandler;
    }
    
    @Override
    public String process(String input) throws LauncherException
    {
        if(input.equals("BATCH_LOC"))
            return launcherHandler.getBatchBaseFolder();
        
        if(input.equals("OUTPUT_LOC"))
            return launcherHandler.getOutputBaseFolder();
        
        return null;
    }
}

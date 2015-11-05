
package hmod.launcher.varproc;

import hmod.launcher.LauncherException;
import hmod.launcher.VariableProcessor;
import hmod.launcher.components.LauncherData;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultFoldersProcessor implements VariableProcessor
{
    private LauncherData launcherHandler;
    
    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    @Override
    public String process(String input) throws LauncherException
    {
        String res = input.replace("BATCH_LOC", launcherHandler.getBatchBaseFolder())
            .replace("OUTPUT_LOC", launcherHandler.getOutputBaseFolder());
        
        return res;
    }
}

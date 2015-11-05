
package hmod.launcher.components;

import hmod.core.BooleanOperator;

/**
 *
 * @author Enrique Urra C.
 */
public class CheckTerminated extends BooleanOperator
{
    private LauncherData launcherHandler;

    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    @Override
    public Boolean evaluate()
    {
        if(launcherHandler.getRunning())
            return false;
        
        return true;
    }    
}

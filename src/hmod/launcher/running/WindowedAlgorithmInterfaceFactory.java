
package hmod.launcher.running;

import hmod.launcher.components.LauncherData;
import hmod.launcher.components.RunData;
import hmod.launcher.components.WindowsData;

/**
 *
 * @author Enrique Urra C.
 */
@InterfaceInfo(
    id = "windowed",
    description = "Interface based on a simple window (JDialog). Supports "
        + "threading through modal mode."
)
public class WindowedAlgorithmInterfaceFactory extends AlgorithmInterfaceFactory
{
    private LauncherData launcherHandler;
    private WindowsData windowsHandler;
    private RunData runHandler;

    public void setLauncherData(LauncherData handler)
    {
        this.launcherHandler = handler;
    }
    
    public void setWindowsData(WindowsData handler)
    {
        this.windowsHandler = handler;
    }

    public void setRunData(RunData handler)
    {
        this.runHandler = handler;
    }
    
    @Override
    public AlgorithmInterface createInterface()
    {        
        return new WindowedAlgorithmInterface(
            //launcherHandler.getStartId(), 
            runHandler.getThreading(), 
            windowsHandler.getWindowAutoClose());
    }
}

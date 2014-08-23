
package hmod.domains.launcher.core.running;

import hmod.domains.launcher.core.ac.LauncherHandler;
import hmod.domains.launcher.core.ac.WindowsHandler;

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
    private LauncherHandler launcherHandler;
    private WindowsHandler windowsHandler;

    public void setLauncherHandler(LauncherHandler launcherHandler)
    {
        this.launcherHandler = launcherHandler;
    }

    public void setWindowsHandler(WindowsHandler windowsHandler)
    {
        this.windowsHandler = windowsHandler;
    }
    
    @Override
    public AlgorithmInterface createInterface(String algName)
    {        
        return new WindowedAlgorithmInterface(
            algName, 
            launcherHandler.isThreadingEnabled(), 
            windowsHandler.isAutoCloseEnabled());
    }
}

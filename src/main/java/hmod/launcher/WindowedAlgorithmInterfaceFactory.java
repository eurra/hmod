
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
@InterfaceInfo(
    id = "windowed",
    description = "Interface based on a simple window (JDialog). Supports "
        + "threading through modal mode."
)
class WindowedAlgorithmInterfaceFactory extends AlgorithmInterfaceFactory
{
    private final LauncherHandler launcherHandler;
    private final WindowsHandler windowsHandler;

    public WindowedAlgorithmInterfaceFactory(LauncherHandler launcherHandler,
                                             WindowsHandler windowsHandler)
    {
        this.launcherHandler = launcherHandler;
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

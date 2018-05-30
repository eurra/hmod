
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
    private final LauncherControl launcherHandler;
    private final WindowsSettings windowsHandler;

    public WindowedAlgorithmInterfaceFactory(LauncherControl launcherHandler,
                                             WindowsSettings windowsHandler)
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

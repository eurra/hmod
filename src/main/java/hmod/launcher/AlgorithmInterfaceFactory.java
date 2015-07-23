
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class AlgorithmInterfaceFactory
{
    private final InterfaceInfo info;
    
    public AlgorithmInterfaceFactory()
    {
        info = this.getClass().getAnnotation(InterfaceInfo.class);
    }

    public final InterfaceInfo getInfo()
    {
        return info;
    }
    
    public abstract AlgorithmInterface createInterface(String algorithmName);
}
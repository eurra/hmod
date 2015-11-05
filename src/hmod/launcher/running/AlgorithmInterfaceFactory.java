
package hmod.launcher.running;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class AlgorithmInterfaceFactory
{
    private InterfaceInfo info;
    
    public AlgorithmInterfaceFactory()
    {
        info = this.getClass().getAnnotation(InterfaceInfo.class);
    }
    
    public String getId()
    {
        if(info != null)
            return info.id();
        
        return null;
    }
    
    public String getDescription()
    {
        if(info != null)
            return info.description();
        
        return null;
    }
    
    public abstract AlgorithmInterface createInterface();
}
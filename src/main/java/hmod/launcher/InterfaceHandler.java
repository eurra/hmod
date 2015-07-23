
package hmod.launcher;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
final class InterfaceHandler implements InterfaceRegistry
{
    private final HashMap<String, AlgorithmInterfaceFactory> interfaces = new HashMap<>();
    private String currentSelected = null;

    public InterfaceHandler(String defaultId)
    {
        currentSelected = defaultId;
    }
   
    @Override
    public InterfaceHandler addFactory(AlgorithmInterfaceFactory factory)
    {
        InterfaceInfo info = Objects.requireNonNull(factory, "null factory").getInfo();
        
        if(info == null)
            throw new NullPointerException("Null interface info for factory '" + factory.getClass() + "'");
        
        String id = info.id();
        
        if(id == null || id.isEmpty())
            throw new IllegalArgumentException("The interface factory '" + factory.getClass() + "' does not defined a valid identifier.");
        
        if(interfaces.containsKey(id))
            throw new IllegalArgumentException("The id of the interface factory '" + factory.getClass() + "' is already registered.");
        
        interfaces.put(id, factory);
        return this;
    }
    
    @Override
    public InterfaceHandler addFactories(AlgorithmInterfaceFactory... factories)
    {
        for(AlgorithmInterfaceFactory factory : factories)
            addFactory(factory);
        
        return this;
    }
    
    @Override
    public boolean interfaceExists(String id)
    {
        return interfaces.containsKey(id);
    }
    
    private AlgorithmInterfaceFactory getFactory(String id)
    {
        if(!interfaceExists(id))
            throw new IllegalArgumentException("The runner id '" + id + "' is not registered");
        
        return interfaces.get(id);
    }

    public void setCurrentInterface(String id) throws IllegalArgumentException
    {
        if(!interfaceExists(id))
            throw new IllegalArgumentException("The interface id '" + id + "' is not registered");
        
        currentSelected = id;
    }

    public InterfaceInfo getCurrentInterfaceInfo() throws IllegalArgumentException
    {
        return getFactory(currentSelected).getInfo();
    }

    public AlgorithmInterface createNewInterfaceFromCurrent(String algName) throws IllegalArgumentException
    {
        return getFactory(currentSelected).createInterface(algName);
    }

    public String[] getSupportedInterfacesIds()
    {
        return interfaces.keySet().toArray(new String[0]);
    }

    public InterfaceInfo getInterfaceInfoFor(String id) throws IllegalArgumentException
    {
        return getFactory(id).getInfo();
    }
}

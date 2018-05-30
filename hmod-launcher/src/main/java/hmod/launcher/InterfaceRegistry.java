
package hmod.launcher;

import hmod.ap.FindOperations;
import hmod.ap.Operator;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public final class InterfaceRegistry
{
    private final HashMap<String, AlgorithmInterfaceFactory> interfaces = new HashMap<>();
    private String currentSelected = null;

    InterfaceRegistry(String defaultId)
    {
        currentSelected = defaultId;
    }
   
    public InterfaceRegistry addFactory(AlgorithmInterfaceFactory factory)
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
    
    @Operator
    public InterfaceRegistry addFactories(AlgorithmInterfaceFactory... factories)
    {
        for(AlgorithmInterfaceFactory factory : factories)
            addFactory(factory);
        
        return this;
    }
    
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

    void setCurrentInterface(String id) throws IllegalArgumentException
    {
        if(!interfaceExists(id))
            throw new IllegalArgumentException("The interface id '" + id + "' is not registered");
        
        currentSelected = id;
    }

    InterfaceInfo getCurrentInterfaceInfo() throws IllegalArgumentException
    {
        return getFactory(currentSelected).getInfo();
    }

    AlgorithmInterface createNewInterfaceFromCurrent(String algName) throws IllegalArgumentException
    {
        return getFactory(currentSelected).createInterface(algName);
    }

    String[] getSupportedInterfacesIds()
    {
        return interfaces.keySet().toArray(new String[0]);
    }

    InterfaceInfo getInterfaceInfoFor(String id) throws IllegalArgumentException
    {
        return getFactory(id).getInfo();
    }
}

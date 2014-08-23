
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.running.AlgorithmInterface;
import hmod.domains.launcher.core.running.AlgorithmInterfaceFactory;
import hmod.domains.launcher.core.running.InterfaceInfo;
import java.util.HashMap;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultInterfaceHandler implements RunInterfaceHandler
{
    private HashMap<String, AlgorithmInterfaceFactory> interfaces;
    private String currentSelected = null;

    public DefaultInterfaceHandler(AlgorithmInterfaceFactory[] factories)
    {
        this(factories, null);
    }

    public DefaultInterfaceHandler(AlgorithmInterfaceFactory[] factories, String defaultId)
    {
        if (factories == null || factories.length == 0)
        {
            throw new IllegalArgumentException("Null or empty interface factories array");
        }
        this.interfaces = new HashMap<>(factories.length);
        boolean defaultExists = false;
        for (int i = 0; i < factories.length; i++)
        {
            AlgorithmInterfaceFactory factory = factories[i];
            if (factory == null)
            {
                throw new NullPointerException("Null interface factory at position " + i);
            }
            InterfaceInfo info = factory.getInfo();
            if (info == null)
            {
                throw new NullPointerException("Null interface info for factory '" + factory.getClass() + "'");
            }
            String id = info.id();
            if (id == null || id.isEmpty())
            {
                throw new IllegalArgumentException("The interface factory '" + factory.getClass() + "' does not defined a valid identifier.");
            }
            if (interfaces.containsKey(id))
            {
                throw new IllegalArgumentException("The id of the interface factory '" + factory.getClass() + "' is already registered.");
            }
            interfaces.put(id, factory);
            if (defaultId == null)
            {
                defaultId = id;
            }
            if (id.equals(defaultId))
            {
                defaultExists = true;
            }
        }
        if (!defaultExists)
        {
            throw new IllegalArgumentException("The default interface id '" + defaultId + "' has not been registered");
        }
        else
        {
            currentSelected = defaultId;
        }
    }

    @Override
    public void setCurrentInterface(String id) throws DataHandlingException
    {
        if (!interfaces.containsKey(id))
        {
            throw new DataHandlingException("The interface id '" + id + "' is not registered");
        }
        currentSelected = id;
    }

    @Override
    public InterfaceInfo getCurrentInterfaceInfo() throws DataHandlingException
    {
        return interfaces.get(currentSelected).getInfo();
    }

    @Override
    public AlgorithmInterface createNewInterfaceFromCurrent(String algName) throws DataHandlingException
    {
        return interfaces.get(currentSelected).createInterface(algName);
    }

    @Override
    public String[] getSupportedInterfacesIds()
    {
        return interfaces.keySet().toArray(new String[0]);
    }

    @Override
    public InterfaceInfo getInterfaceInfoFor(String id) throws DataHandlingException
    {
        if (!interfaces.containsKey(id))
        {
            throw new DataHandlingException("The interface id '" + id + "' is not registered");
        }
        return interfaces.get(id).getInfo();
    }
    
}

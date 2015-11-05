
package hmod.launcher;

import hmod.launcher.running.AlgorithmInterfaceFactory;
import hmod.launcher.running.AlgorithmRunnerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public final class RunManager
{
    private Map<String, AlgorithmRunnerFactory> runners;
    private Map<String, AlgorithmInterfaceFactory> interfaces;
    private String currentRunner;
    private String currentInterface;

    public RunManager()
    {
        this.runners = new HashMap<>();
        this.interfaces = new HashMap<>();
    }
    
    public void registerRunnerFactory(AlgorithmRunnerFactory factory) throws LauncherException
    {
        String id = factory.getId();
        
        if(id == null)
            throw new LauncherException("The provided factory does not have a valid identifier.");
        
        if(runners.containsKey(id))
            throw new LauncherException("The id of the provided factory is already registered.");
        
        runners.put(id, factory);
        
        if(currentRunner == null)
            currentRunner = id;
    }
    
    public void registerInterfaceFactory(AlgorithmInterfaceFactory factory) throws LauncherException
    {
        String id = factory.getId();
        
        if(id == null)
            throw new LauncherException("The provided factory does not have a valid identifier.");
        
        if(interfaces.containsKey(id))
            throw new LauncherException("The id of the provided factory is already registered.");
        
        interfaces.put(id, factory);
        
        if(currentInterface == null)
            currentInterface = id;
    }
    
    public boolean setCurrentRunnerFactory(String id)
    {
        if(runners.containsKey(id))
        {
            currentRunner = id;
            return true;
        }
        
        return false;
    }
    
    public AlgorithmRunnerFactory getCurrentRunnerFactory()
    {
        if(currentRunner == null)
            return null;
        
        return runners.get(currentRunner);
    }
    
    public boolean setCurrentInterfaceFactory(String id)
    {
        if(interfaces.containsKey(id))
        {
            currentInterface = id;
            return true;
        }
        
        return false;
    }
    
    public AlgorithmInterfaceFactory getCurrentInterfaceFactory()
    {
        if(currentInterface == null)
            return null;
        
        return interfaces.get(currentInterface);
    }
    
    public AlgorithmRunnerFactory getRunnerFactory(String id)
    {
        return runners.get(id);
    }
    
    public AlgorithmInterfaceFactory getInterfaceFactory(String id)
    {
        return interfaces.get(id);
    }
    
    public String[] getRunnerIds()
    {
        return runners.keySet().toArray(new String[0]);
    }
    
    public String[] getInterfaceIds()
    {
        return interfaces.keySet().toArray(new String[0]);
    }
}

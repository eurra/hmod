
package hmod.launcher;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
final class RunnerHandler implements RunnerRegistry
{
    private final HashMap<String, AlgorithmRunnerFactory> runners = new HashMap<>();
    private String currentSelected = null;

    public RunnerHandler(String defaultId)
    {
        currentSelected = defaultId;
    }
    
    @Override
    public RunnerHandler addFactory(AlgorithmRunnerFactory factory)
    {
        RunnerInfo info = Objects.requireNonNull(factory, "null factory").getInfo();
        
        if(info == null)
            throw new NullPointerException("Null runner info for factory '" + factory.getClass() + "'");
        
        String id = info.id();
        
        if(id == null || id.isEmpty())
            throw new IllegalArgumentException("The runner factory '" + factory.getClass() + "' does not defined a valid identifier.");
        
        if(runners.containsKey(id))
            throw new IllegalArgumentException("The id of the runner factory '" + factory.getClass() + "' is already registered.");
        
        runners.put(id, factory);
        return this;
    }
    
    @Override
    public RunnerHandler addFactories(AlgorithmRunnerFactory... factories)
    {
        for(AlgorithmRunnerFactory factory : factories)
            addFactory(factory);
        
        return this;
    }
    
    @Override
    public boolean runnerExists(String id)
    {
        return runners.containsKey(id);
    }

    public void setCurrentRunner(String id) throws IllegalArgumentException
    {
        if(!runnerExists(id))
            throw new IllegalArgumentException("The runner id '" + id + "' is not registered");
        
        currentSelected = id;
    }
    
    private AlgorithmRunnerFactory getFactory(String id)
    {
        if(!runnerExists(id))
            throw new IllegalArgumentException("The runner id '" + id + "' is not registered");
        
        return runners.get(id);
    }

    public RunnerInfo getCurrentRunnerInfo() throws IllegalArgumentException
    {
        return getFactory(currentSelected).getInfo();
    }

    public AlgorithmRunner createNewRunnerFromCurrent() throws IllegalArgumentException
    {
        return getFactory(currentSelected).createRunner();
    }

    public String[] getSupportedRunnersIds()
    {
        return runners.keySet().toArray(new String[0]);
    }

    public RunnerInfo getRunnerInfoFor(String id) throws IllegalArgumentException
    {
        return getFactory(id).getInfo();
    }
}

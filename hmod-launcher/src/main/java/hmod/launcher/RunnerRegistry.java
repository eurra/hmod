
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
public final class RunnerRegistry
{
    private final HashMap<String, AlgorithmRunnerFactory> runners = new HashMap<>();
    private String currentSelected = null;

    RunnerRegistry(String defaultId)
    {
        currentSelected = defaultId;
    }
    
    public RunnerRegistry addFactory(AlgorithmRunnerFactory factory)
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
    
    @Operator
    public RunnerRegistry addFactories(AlgorithmRunnerFactory... factories)
    {
        for(AlgorithmRunnerFactory factory : factories)
            addFactory(factory);
        
        return this;
    }
    
    boolean runnerExists(String id)
    {
        return runners.containsKey(id);
    }

    void setCurrentRunner(String id) throws IllegalArgumentException
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

    RunnerInfo getCurrentRunnerInfo() throws IllegalArgumentException
    {
        return getFactory(currentSelected).getInfo();
    }

    AlgorithmRunner createNewRunnerFromCurrent() throws IllegalArgumentException
    {
        return getFactory(currentSelected).createRunner();
    }

    String[] getSupportedRunnersIds()
    {
        return runners.keySet().toArray(new String[0]);
    }

    RunnerInfo getRunnerInfoFor(String id) throws IllegalArgumentException
    {
        return getFactory(id).getInfo();
    }
}

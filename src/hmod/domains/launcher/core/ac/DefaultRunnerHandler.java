
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.running.AlgorithmRunner;
import hmod.domains.launcher.core.running.AlgorithmRunnerFactory;
import hmod.domains.launcher.core.running.RunnerInfo;
import java.util.HashMap;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultRunnerHandler implements RunnerHandler
{
    private HashMap<String, AlgorithmRunnerFactory> runners;
    private String currentSelected = null;

    public DefaultRunnerHandler(AlgorithmRunnerFactory[] factories)
    {
        this(factories, null);
    }

    public DefaultRunnerHandler(AlgorithmRunnerFactory[] factories, String defaultId)
    {
        if (factories == null || factories.length == 0)
        {
            throw new IllegalArgumentException("Null or empty runner factories array");
        }
        this.runners = new HashMap<>(factories.length);
        boolean defaultExists = false;
        for (int i = 0; i < factories.length; i++)
        {
            AlgorithmRunnerFactory factory = factories[i];
            if (factory == null)
            {
                throw new NullPointerException("Null runner factory at position " + i);
            }
            RunnerInfo info = factory.getInfo();
            if (info == null)
            {
                throw new NullPointerException("Null runner info for factory '" + factory.getClass() + "'");
            }
            String id = info.id();
            if (id == null || id.isEmpty())
            {
                throw new IllegalArgumentException("The runner factory '" + factory.getClass() + "' does not defined a valid identifier.");
            }
            if (runners.containsKey(id))
            {
                throw new IllegalArgumentException("The id of the runner factory '" + factory.getClass() + "' is already registered.");
            }
            runners.put(id, factory);
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
            throw new IllegalArgumentException("The default runner id '" + defaultId + "' has not been registered");
        }
        else
        {
            currentSelected = defaultId;
        }
    }

    @Override
    public void setCurrentRunner(String id) throws DataHandlingException
    {
        if (!runners.containsKey(id))
        {
            throw new DataHandlingException("The runner id '" + id + "' is not registered");
        }
        currentSelected = id;
    }

    @Override
    public RunnerInfo getCurrentRunnerInfo() throws DataHandlingException
    {
        return runners.get(currentSelected).getInfo();
    }

    @Override
    public AlgorithmRunner createNewRunnerFromCurrent() throws DataHandlingException
    {
        return runners.get(currentSelected).createRunner();
    }

    @Override
    public String[] getSupportedRunnersIds()
    {
        return runners.keySet().toArray(new String[0]);
    }

    @Override
    public RunnerInfo getRunnerInfoFor(String id) throws DataHandlingException
    {
        if (!runners.containsKey(id))
        {
            throw new DataHandlingException("The runner id '" + id + "' is not registered");
        }
        return runners.get(id).getInfo();
    }
    
}

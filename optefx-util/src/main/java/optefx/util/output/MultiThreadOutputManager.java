
package optefx.util.output;

import java.io.PrintWriter;
import java.util.WeakHashMap;

public class MultiThreadOutputManager extends OutputManager
{
    private WeakHashMap<Thread, OutputManager> threadsMap;

    public MultiThreadOutputManager()
    {
        this.threadsMap = new WeakHashMap<>();
    }
    
    @Override
    public final MultiThreadOutputManager init()
    {
        threadsMap.clear();
        addManager(Thread.currentThread(), OutputManager.getBaseManager());
        
        return this;
    }
    
    private void addManager(Thread thread, OutputManager manager)
    {
        threadsMap.put(thread, manager);
    }
    
    private OutputManager getManagerForThread()
    {
        Thread currThread = Thread.currentThread();
        OutputManager manager = threadsMap.get(currThread);
        
        if(manager == null)
        {
            manager = new DefaultOutputManager();
            addManager(currThread, manager);
        }
        
        return manager;
    }

    @Override
    public MultiThreadOutputManager setOutputsFromConfig(OutputConfig config)
    {
        getManagerForThread().setOutputsFromConfig(config);
        return this;
    }

    @Override
    public PrintWriter getOutput(String id)
    {
        return getManagerForThread().getOutput(id);
    }

    @Override
    public MultiThreadOutputManager closeOutputs()
    {
        getManagerForThread().closeOutputs();
        return this;
    }

    @Override
    public MultiThreadOutputManager closeOutputs(String id)
    {
        getManagerForThread().clearOutputs(id);
        return this;
    }

    @Override
    public MultiThreadOutputManager clearOutputs()
    {
        getManagerForThread().clearOutputs();
        return this;
    }

    @Override
    public MultiThreadOutputManager clearOutputs(String id)
    {
        getManagerForThread().clearOutputs(id);
        return this;
    }
}
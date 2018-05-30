
package optefx.util.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Implements a multi-thread based random tool, which associates a different 
 * Random object for each thread on which the manager was used. This 
 * implementation also performs a clean-up of random objects associated with 
 * dead threads every a number of times the manager has been used. 
 * @author Enrique Urra C.
 */
class MultiThreadRandomTool extends RandomTool
{
    /**
     * A table that relates the registered threads with the random items.
     */
    private Map<Thread, RandomItem> threadsTable;
    /**
     * An access' counter, for internal cleanup.
     */
    private int accessCount;

    /**
     * Default constructor.
     */
    public MultiThreadRandomTool()
    {
        threadsTable = new HashMap<Thread, RandomItem>();
    }
    
    /**
     * Performs a clean-up of random objects associated with dead threads.
     */
    private void cleanUp()
    {
        ArrayList<Thread> deadThreads = new ArrayList<Thread>(threadsTable.size());
        
        for(Thread thread : threadsTable.keySet())
        {
            if(!thread.isAlive())
                deadThreads.add(thread);
        }
        
        int count = deadThreads.size();
        
        for(int i = 0; i < count; i++)
        {
            Thread thread = deadThreads.get(i);
            threadsTable.remove(thread);
        }
    }
    
    /**
     * Gets (and sets if no exist) the random item of the current thread.
     * @return The random item.
     */
    private RandomItem getItem()
    {
        Thread currThread = Thread.currentThread();
        RandomItem item = threadsTable.get(currThread);
        
        if(item == null)
        {
            item = new RandomItem(createRandomSeed());
            threadsTable.put(currThread, item);
        }
        
        if(++accessCount == 10)
        {
            cleanUp();
            accessCount = 0;
        }
        
        return item;
    }
    
    @Override
    public Random getRandom()
    {
        return getItem().getRandom();
    }
    
    @Override
    public final long createRandomSeed()
    {
        return System.currentTimeMillis();
    }

    @Override
    public void setSeed(long seed)
    {
        getItem().setSeed(seed);
    }

    @Override
    public long getSeed()
    {
        return getItem().getSeed();
    }
}
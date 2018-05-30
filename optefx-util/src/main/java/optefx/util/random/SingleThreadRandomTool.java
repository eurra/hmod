
package optefx.util.random;

import java.util.Random;

/**
 * Implements a single-thread random tool, which encapsulates a single random
 * instance.
 * @author Enrique Urra C.
 */
class SingleThreadRandomTool extends RandomTool
{
    /**
     * The internal random instance.
     */
    private RandomItem item;

    /**
     * The default constructor.
     */
    public SingleThreadRandomTool()
    {
        item = new RandomItem(createRandomSeed());
    }
    
    @Override
    public Random getRandom()
    {
        return item.getRandom();
    }

    @Override
    public final long createRandomSeed()
    {
        return System.currentTimeMillis();
    }

    @Override
    public void setSeed(long seed)
    {
        item.setSeed(seed);
    }

    @Override
    public long getSeed()
    {
        return item.getSeed();
    }
}
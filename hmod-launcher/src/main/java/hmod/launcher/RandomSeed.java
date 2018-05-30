
package hmod.launcher;

import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public final class RandomSeed
{
    private long curr;

    RandomSeed()
    {
        setRandom();
    }
    
    public void setCurrent(long seed)
    {
        curr = seed;
    }

    public void setRandom()
    {
        curr = RandomTool.getInstance().createRandomSeed();
    }

    public long getCurrent()
    {
        return curr;
    }
}

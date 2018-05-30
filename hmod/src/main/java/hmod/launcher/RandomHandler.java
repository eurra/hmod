
package hmod.launcher;

import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public final class RandomHandler
{
    private long currSeed;

    RandomHandler()
    {
        setRandom();
    }

    private void setRandom()
    {
        currSeed = RandomTool.getInstance().createRandomSeed();
    }

    public void setCurrentSeed(long seed)
    {
        currSeed = seed;
    }

    public void setRandomSeed()
    {
        setRandom();
    }

    public long getCurrentSeed()
    {
        return currSeed;
    }
}

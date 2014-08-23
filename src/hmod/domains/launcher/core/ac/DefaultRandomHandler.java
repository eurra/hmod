
package hmod.domains.launcher.core.ac;

import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultRandomHandler implements RandomHandler
{
    private long currSeed;

    public DefaultRandomHandler()
    {
        setRandom();
    }

    private void setRandom()
    {
        currSeed = RandomTool.getInstance().createRandomSeed();
    }

    @Override
    public void setCurrentSeed(long seed)
    {
        currSeed = seed;
    }

    @Override
    public void setRandomSeed()
    {
        setRandom();
    }

    @Override
    public long getCurrentSeed()
    {
        return currSeed;
    }
    
}


package optefx.util.random;

import java.util.Random;

/**
 * Implements a beans which mantains a random object and its seed.
 * @author Enrique Urra C.
 */
final class RandomItem
{
    private Random random;
    private long seed;

    public RandomItem(long seed)
    {
        this.random = new Random(seed);
        this.seed = seed;
    }

    public Random getRandom()
    {
        return random;
    }

    public long getSeed()
    {
        return seed;
    }

    public void setSeed(long seed)
    {
        this.seed = seed;
        this.random.setSeed(seed);
    }
}

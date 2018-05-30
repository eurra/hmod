package optefx.util.random;

import java.lang.reflect.Array;
import java.util.Random;

/**
 * Provides a environment tool for pseudo-random number generation. The main
 * method of this class (getRandom()) provides a centralized Random object 
 * reference. Several static methods are provided as shortcuts to access the 
 * common operations in the Random class. For experiment replicability, there is
 * also helper methods to configure the random seed.
 * 
 * This class is based in the singleton pattern, through which two different 
 * implementations can be used:
 * - A single thread implementation, which allows to perform faster random 
 *   number generation. As this implementation runs in a single thread, the 
 *   usage of concurrent algorithms that access to the random tool is 
 *   discouraged, if any further replication based on the random seed is needed.
 * - A multiple thread implementation, which allows to associate a particular
 *   Random instance to the current executing thread. Because the thread 
 *   management, this implementation is only recommended for develop or required
 *   multi-thread environments.
 * 
 * Both implementations can be switched through the setMode() method.
 * 
 * @author Enrique Urra C.
 */
public abstract class RandomTool
{
    /**
     * Single thread flag for random generation.
     */
    public static final int MODE_SINGLE_THREAD = 0;
    /**
     * Multiple thread flag random generation.
     */
    public static final int MODE_MULTI_THREAD = 1;
    
    /**
     * The internal (unique) instance.
     */
    private static RandomTool instance;
    /**
     * The current threading mode
     */
    private static int currentMode = -1;
    
    /**
     * Gets the unique instance of the tool.
     * @return The tool object.
     */
    public static RandomTool getInstance()
    {
        if(instance == null)
            setMode(MODE_SINGLE_THREAD);
        
        return instance;
    }
    
    /**
     * Configures the theading mode of the random tool.
     * @param mode The mode identifier. Check the constants in this class.
     */
    public static void setMode(int mode)
    {
        if(mode == currentMode)
            return;
        
        switch(mode)
        {
            case MODE_SINGLE_THREAD : 
            {
                instance = new SingleThreadRandomTool();
                break;
            } 
            case MODE_MULTI_THREAD :
            {
                instance = new MultiThreadRandomTool();
                break;
            }
            default : 
            {
                throw new IllegalArgumentException("The provided mode is not valid.");
            }
        }
        
        currentMode = mode;
    }
    
    /**
     * Gets a random integer between zero and the provided maximum (exclusive)  
     * from the current random object.
     * @param max The maximum retrievable value (exclusive).
     * @return The random int obtained.
     */
    public static int getInt(int max)
    {
        Random random = getInstance().getRandom();
        return random.nextInt(max);
    }
    
    /**
     * Gets a random double number between 0.0 and 1.0 from the current object.
     * @return The random double obtained.
     */
    public static double getDouble()
    {
        Random random = getInstance().getRandom();
        return random.nextDouble();
    }
    
    /**
     * Gets a random boolean value from the current random object.
     * @return The random boolean obtained.
     */
    public static boolean getBoolean()
    {
        Random random = getInstance().getRandom();
        return random.nextBoolean();
    }
    
    /**
     * General implementation of the Fisher–Yates shuffle algorithm
     * (http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle).
     * @param input The input array.
     * @return A shuffled array copy.
     */
    public static <T> T[] fastArrayShuffle(T[] input)
    {
        if(input == null)
            return null;
        
        if(input.length == 0)
            return (T[])Array.newInstance(input.getClass().getComponentType(), 0);
        
        T[] res = (T[])Array.newInstance(input.getClass().getComponentType(), input.length);
        res[0] = input[0];
        
        for(int i = 1; i < input.length; i++)
        {
            int rand = getInt(i + 1);
            res[i] = res[rand];
            res[rand] = input[i];
        }
        
        return res;
    }
    
    /**
     * Gets the current random object configured in the tool.
     * @return The random object.
     */
    public abstract Random getRandom();
    
    /**
     * Generates a new random seed that can be used with this tool.
     * @return The generated seed.
     */
    public abstract long createRandomSeed();
    
    /**
     * Sets the seed of the current random object.
     * @param seed The seed to set.
     */
    public abstract void setSeed(long seed);
    
    /**
     * Gets the seed of the current random object.
     * @return The seed as long.
     */
    public abstract long getSeed();
}
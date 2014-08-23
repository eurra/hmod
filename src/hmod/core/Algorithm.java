
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public interface Algorithm
{
    public static Algorithm create(Step... steps)
    {
        if(steps.length == 0)
            throw new IllegalArgumentException("No steps were provided");
        
        if(steps.length < 2)
            return new SimpleAlgorithm(steps[0]);
        else
            return new SequentialAlgorithm(steps);
    }
    
    void start() throws AlgorithmException;
    void stop() throws AlgorithmException;
}

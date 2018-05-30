
package hmod.domains.mkp;

/**
 *
 * @author Enrique Urra C.
 */
public interface MKPProblemInstance
{  
    int getNumber();
    int getItemsCount();
    int getResourcesCount();    
    Item getItem(int itemId);
    Resource getResource(int resourceId);
    boolean itemExists(Item item);
    boolean resourceExists(Resource res);
    void checkItem(Item item) throws IllegalArgumentException;
    void checkResource(Resource res) throws IllegalArgumentException;
    int getWeight(Item item, Resource resource);
    
    default double getLPOptimum()
    {
        return -1.0;
    }
    
    default boolean isLPOptimumAvailable()
    {
        return false;
    }
    
    default double getGapFor(double solutionValue)
    {
        if(!isLPOptimumAvailable())
            return -1.0;
        
        double lpOptimum = getLPOptimum();
        return (lpOptimum - solutionValue) / lpOptimum;
    }
    
    default double getPorcentualGapFor(double solutionValue)
    {
        return getGapFor(solutionValue) * 100;
    }
}


package hmod.domains.mkp;

/**
 *
 * @author Enrique Urra C.
 */
public interface MKPSolution
{
    MKPProblemInstance getInstance();
    int getTotalProfit();
    Item[] getItems();
    int[] getResourceUsage();
    boolean isFeasible();
    boolean hasItem(int id);
    int getItemsCount();
    boolean sameAs(MKPSolution other);
    
    default boolean isGapAvailable()
    {
        return getInstance().isLPOptimumAvailable();
    }
        
    default double getGap()
    {
        return getInstance().getGapFor(getTotalProfit());
    }
    
    default double getPorcentualGap()
    {
        return getInstance().getPorcentualGapFor(getTotalProfit());
    }
}

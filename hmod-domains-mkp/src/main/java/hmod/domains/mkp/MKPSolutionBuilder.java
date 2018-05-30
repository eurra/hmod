
package hmod.domains.mkp;

/**
 *
 * @author Enrique Urra C.
 */
public interface MKPSolutionBuilder
{
    void includeItem(Item item) throws IllegalArgumentException;
    void excludeItem(Item item) throws IllegalArgumentException;
    boolean isIncludeFeasible(Item item);
    boolean isItemIncluded(Item item);
    Item[] getIncludedItems();
    Item[] getAvailableItems();
    int getIncludedCount();
    int getAvailableCount();
    boolean isFeasible();
    MKPSolution build();
    void clear();
}

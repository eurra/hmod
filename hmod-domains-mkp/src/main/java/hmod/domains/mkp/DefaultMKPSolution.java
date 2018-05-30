
package hmod.domains.mkp;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultMKPSolution implements MKPSolution
{
    private final int totalProfit;
    private final int constraintViolation;
    private final Item[] items;
    private final int[] resourceUsage;
    private final MKPProblemInstance instance;
    private final Set<Integer> itemIds;

    public DefaultMKPSolution(MKPProblemInstance instance, int totalProfit, int constraintViolation, Item[] items, int[] resourceUsage)
    {
        this.instance = instance;
        this.totalProfit = totalProfit;
        this.constraintViolation = constraintViolation;
        this.items = items;
        this.resourceUsage = resourceUsage;
        this.itemIds = new HashSet<>(items.length);
        
        for(int i = 0; i < items.length; i++)
            itemIds.add(items[i].getId());
    }

    @Override
    public int getTotalProfit()
    {
        //return totalProfit - constraintViolation * 1000;
        return totalProfit - constraintViolation;
    }

    @Override
    public Item[] getItems()
    {
        Item[] copy = new Item[items.length];
        System.arraycopy(items, 0, copy, 0, copy.length);
        
        return copy;
    }
    
    @Override
    public int[] getResourceUsage()
    {
        int[] copy = new int[resourceUsage.length];
        System.arraycopy(resourceUsage, 0, copy, 0, copy.length);
        
        return copy;
    }

    @Override
    public MKPProblemInstance getInstance()
    {
        return instance;
    }

    @Override
    public boolean isFeasible()
    {
        return constraintViolation == 0;
    }

    @Override
    public boolean hasItem(int id)
    {
        return itemIds.contains(id);
    }

    @Override
    public int getItemsCount()
    {
        return items.length;
    }
    
    @Override
    public boolean sameAs(MKPSolution other)
    {
        if(other.getItemsCount() != getItemsCount())
            return false;
        
        for(Integer itemId : itemIds)
        {
            if(!other.hasItem(itemId))
                return false;
        }
        
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("Total profit: ").append(getTotalProfit());
        
        if(isGapAvailable())
            sb.append(" (gap: ").append(getPorcentualGap()).append(")");
        
        sb.append("\n").append("Feasible: ").append(isFeasible()).append("\n");
        sb.append("Item list: ");
        
        for(int i = 0; i < items.length; i++)
            sb.append(items[i].getId()).append(" ");
        
        sb.append("\nResource usage: ");
        
        for(int i = 0; i < resourceUsage.length; i++)
        {
            Resource res = instance.getResource(i);
            sb.append(resourceUsage[i]).append(res.getCapacity() < resourceUsage[i] ? "!" : "").append(" ");
        }
        
        return sb.append("\n").toString();
    }
}


package hmod.domains.mkp;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * @author Enrique Urra C.
 */
public final class SolutionBuilderHandler implements MKPSolutionBuilder
{
    private final MKPProblemInstance instance;
    private final HashSet<Item> included;
    private final HashSet<Item> available;
    private int[] currResourceUsage;
    private int maxProfit;

    SolutionBuilderHandler(MKPProblemInstance instance)
    {
        if(instance == null)
            throw new NullPointerException("Null instance");
        
        this.instance = instance;
        int itemsCount = instance.getItemsCount();
        included = new HashSet<>(itemsCount);
        available = new HashSet<>(itemsCount);
        maxProfit = calculateMaxProfit();
        
        clear();
    }
    
    private int calculateMaxProfit()
    {
        int itemsCount = instance.getItemsCount();
        Item maxProfitItem = null;
        
        for(int i = 0; i < itemsCount; i++)
        {
            Item currItem = instance.getItem(i);
            
            if(maxProfitItem == null || maxProfitItem.getProfit() < currItem.getProfit())
                maxProfitItem = currItem;
        }
        
        return maxProfitItem.getProfit();
    }
    
    private void addWeight(Item item)
    {
        for(int i = 0; i < currResourceUsage.length; i++)
            currResourceUsage[i] += instance.getWeight(item, instance.getResource(i));
    }
    
    private void removeWeightTo(int[] resourceUsage, Item item)
    {
        for(int i = 0; i < resourceUsage.length; i++)
            resourceUsage[i] -= instance.getWeight(item, instance.getResource(i));
    }
    
    private int getConstraintViolation(int[] resourceUsage)
    {
        int overFilledCount = 0;
        
        for(int i = 0; i < resourceUsage.length; i++)
        {
            Resource res = instance.getResource(i);
            
            if(res.getCapacity() < resourceUsage[i])
                overFilledCount++;
        }
        
        return overFilledCount * included.size() * (maxProfit + 1);
        
        /*int constraintViolation = 0;
        
        for(int i = 0; i < resourceUsage.length; i++)
        {
            Resource res = instance.getResource(i);
            
            if(res.getCapacity() < resourceUsage[i])
                constraintViolation += resourceUsage[i] - res.getCapacity();
        }
        
        return constraintViolation;*/
    }

    @Override
    public void includeItem(Item item) throws IllegalArgumentException
    {
        if(included.contains(item))
            throw new IllegalArgumentException("The provided item is already added");
        
        instance.checkItem(item);
        included.add(item);
        available.remove(item);
        addWeight(item);
    }
    
    @Override
    public void excludeItem(Item item) throws IllegalArgumentException
    {
        if(!included.contains(item))
            throw new IllegalArgumentException("The provided item has not been added");
        
        included.remove(item);
        available.add(item);
        removeWeightTo(currResourceUsage, item);
    }
    
    @Override
    public boolean isIncludeFeasible(Item item)
    {
        for(int i = 0; i < currResourceUsage.length; i++)
        {
            Resource res = instance.getResource(i);
            
            if(currResourceUsage[i] + instance.getWeight(item, res) > res.getCapacity())
                return false;
        }
        
        return true;
    }

    @Override
    public boolean isItemIncluded(Item item)
    {
        return included.contains(item);
    }

    @Override
    public Item[] getIncludedItems()
    {
        return included.toArray(new Item[0]);
    }
    
    @Override
    public Item[] getAvailableItems()
    {
        return available.toArray(new Item[0]);
    }
    

    @Override
    public int getIncludedCount()
    {
        return included.size();
    }

    @Override
    public int getAvailableCount()
    {
        return available.size();
    }

    @Override
    public boolean isFeasible()
    {
        return getConstraintViolation(currResourceUsage) > 0;
    }

    @Override
    public MKPSolution build()
    {
        Item[] finalItems = new Item[included.size()];
        int totalProfit = 0;
        int pos = 0;
        
        for(Item item : included)
        {
            finalItems[pos++] = item;
            totalProfit += item.getProfit();
        }
        
        int constraintViolation = getConstraintViolation(currResourceUsage);        
        return new DefaultMKPSolution(instance, totalProfit, constraintViolation, finalItems, Arrays.copyOf(currResourceUsage, currResourceUsage.length));
    }
    
    public void importSolution(MKPSolution input)
    {
        clear();
        Item[] inputItems = input.getItems();
        
        for(int i = 0; i < inputItems.length; i++)
            includeItem(inputItems[i]);
    }

    @Override
    public final void clear()
    {
        available.clear();
        included.clear();
        currResourceUsage = new int[instance.getResourcesCount()];
        
        int itemsCount = instance.getItemsCount();
        
        for(int i = 0; i < itemsCount; i++)
            available.add(instance.getItem(i));
    }
    
    public boolean checkIfCanAdd()
    {
        return included.size() < instance.getItemsCount();
    }
    
    public boolean checkIfCanRemove()
    {
        return included.size() > 1;
    }
}

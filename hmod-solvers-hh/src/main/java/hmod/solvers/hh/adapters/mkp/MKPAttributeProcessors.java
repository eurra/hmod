
package hmod.solvers.hh.adapters.mkp;

import hmod.domains.mkp.MKPProblemInstance;
import hmod.domains.mkp.MKPSolution;
import hmod.solvers.hh.models.attr.AttributeRegister;
import java.util.HashMap;

/**
 *
 * @author Enrique Urra C.
 */
public final class MKPAttributeProcessors
{    
    private static final HashMap<Integer, Object> itemPresentAttributes = new HashMap<>();
    private static final HashMap<Integer, Object> itemNonPresentAttributes = new HashMap<>();
    private static final HashMap<String, Object> itemPairsAttributes = new HashMap<>();
    private static final HashMap<String, Object> itemSeqAttributes = new HashMap<>();
    
    private static Object getItemPresentAttribute(int itemId)
    {
        if(!itemPresentAttributes.containsKey(itemId))
            itemPresentAttributes.put(itemId, new Object());
        
        return itemPresentAttributes.get(itemId);
    }
    
    private static Object getItemNonPresentAttribute(int itemId)
    {
        if(!itemNonPresentAttributes.containsKey(itemId))
            itemNonPresentAttributes.put(itemId, new Object());
        
        return itemNonPresentAttributes.get(itemId);
    }
    
    private static Object getItemPairAttribute(int idA, int idB)
    {
        String key = idA + "-" + idB;
        
        if(!itemPairsAttributes.containsKey(key))
            itemPairsAttributes.put(key, new Object());
        
        return itemPairsAttributes.get(key);
    }
    
    private static Object getItemSeqAttribute(int idA, int idB)
    {
        String key = idA + "-" + idB;
        
        if(!itemPairsAttributes.containsKey(key))
            itemPairsAttributes.put(key, new Object());
        
        return itemPairsAttributes.get(key);
    }
    
    public static void generalItemInfoProcessor(MKPSolution solution, AttributeRegister reg)
    {
        MKPProblemInstance instance = solution.getInstance();
        int itemsCount = instance.getItemsCount();
        //Set<Integer> itemsPresent = new HashSet<>(itemsCount);
        
        for(int i = 0; i < itemsCount; i++)
        {
            if(!solution.hasItem(i))
            {
                reg.addAttribute(getItemPresentAttribute(i));
            }
            else
            {
                reg.addAttribute(getItemNonPresentAttribute(i));
                
                if(i > 0 && solution.hasItem(i - 1))
                    reg.addAttribute(getItemSeqAttribute(i - 1, i));
                
                //for(int otherItem : itemsPresent)
                //    reg.addAttribute(getItemPairAttribute(otherItem, i));
                
                //itemsPresent.add(i);
            }
        }
    }
    
    public static void resourceUsageProcessor(MKPSolution solution, AttributeRegister<String> reg)
    {
        MKPProblemInstance instance = solution.getInstance();
        int[] resUsage = solution.getResourceUsage();
        
        for(int i = 0; i < resUsage.length; i++)
        {
            double ratio = (double) resUsage[i] / (double) instance.getResource(i).getCapacity();
            
            if(ratio > 0.75)
                reg.addAttribute("resourceUsageOver75(" + i + ")");
            else if(ratio > 0.5)
                reg.addAttribute("resourceUsageOver50(" + i + ")");
            else if(ratio > 0.25)
                reg.addAttribute("resourceUsageOver25(" + i + ")");
            else
                reg.addAttribute("resourceUsageBelow25(" + i + ")");
        }
    }
}

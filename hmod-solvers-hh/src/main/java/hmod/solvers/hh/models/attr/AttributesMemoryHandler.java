
package hmod.solvers.hh.models.attr;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Implements the default attributive memory.
 * @author Enrique Urra C.
 */
public final class AttributesMemoryHandler<T>
{
    private final HashMap<T, Integer> frequencyMemory;
    private final HashMap<T, Integer> recencyMemory;
    private int readingsCount;

    public AttributesMemoryHandler()
    {
        this.frequencyMemory = new HashMap<>();
        this.recencyMemory = new HashMap<>();
        this.readingsCount = 0;
    }
    
    /**
     * Returns the size of the sample from which the frecuency calculations have
     * been done. For example, the count of checked solutions is a common sample
     * size.
     * @return The size as a number.
     */
    public int getSampleSize()
    {
        return readingsCount;
    }

    /**
     * Indicates if the memory contains an attribute with the specified id.
     * @param attr The id of the attribute.
     * @return true if the memory contains the attribute, false otherwise.
     */
    public boolean hasAttribute(T attr)
    {
        return frequencyMemory.containsKey(attr);
    }

    /**
     * Registers a new reading for the specified attribute. By calling this 
     * method, the ocurrence of the attribute within the search is notified.
     * @param attr The id of the attribute.
     * @param iteration The number of the iteration on which the reading has 
     *  been collected.
     */
    public void addReading(T attr, int iteration)
    {
        incrementAttr(attr, iteration);
        readingsCount++;
    }
    
    /**
     * Registers a collection of readings at a specific iteration.
     * @param collection The attribute readings collection.
     * @param iteration The number of the iteration on which the readings have 
     *  been collected.
     */
    public void addReading(AttributesCollection<T> collection, int iteration)
    {
        if(collection == null)
            throw new NullPointerException("The provided collection is null");
        
        for(T attr : collection)
            incrementAttr(attr, iteration);
        
        readingsCount++;
    }
    
    /**
     * Unregisters a reading ocurrence for the specified attribute.
     * @param attr The id of the attribute.
     */
    public void removeReading(T attr)
    {
        decrementAttr(attr);
        readingsCount--;
    }

    /**
     * Unregisters a collection of reading occurences.
     * @param collection The attribute readings collection.
     */
    public void removeReading(AttributesCollection<T> collection)
    {
        if(collection == null)
            throw new NullPointerException("The provided collection is null");
        
        for(T attr : collection)
            decrementAttr(attr);
        
        readingsCount--;
    }
    
    private void incrementAttr(T attr, int iteration)
    {
        Integer currAttrCount = frequencyMemory.get(attr);
        
        if(currAttrCount == null)
            currAttrCount = 0;
        
        currAttrCount++;
        
        if(currAttrCount > 25){
            int a = 0;}
        
        frequencyMemory.put(attr, currAttrCount);
        recencyMemory.put(attr, iteration);
    }
    
    private void decrementAttr(T attr)
    {
        Integer currAttrCount = frequencyMemory.get(attr);
        
        if(currAttrCount == null || currAttrCount == 0)
            return;
        
        currAttrCount--;
        
        if(currAttrCount == 0)
            frequencyMemory.remove(attr);
        else
            frequencyMemory.put(attr, currAttrCount);
    }

    /**
     * Gets a porcentual frequence measure of the specified attribute.
     * @param attr The id of the attribute.
     * @return The measure as percentage.
     */
    public double getReadingFrequency(T attr)
    {
        Integer currAttrCount = frequencyMemory.get(attr);
        
        if(currAttrCount == null)
            return 0.0;
        
        return (double)currAttrCount / (double)readingsCount;
    }

    /**
     * Gets the total readings count of the specified attribute.
     * @param attr The id of the attribute.
     * @return The readings quantity.
     */
    public int getAttrReadingsCount(T attr)
    {
        Integer currAttrCount = frequencyMemory.get(attr);
        
        if(currAttrCount == null)
            return 0;
        
        return currAttrCount;
    }

     /**
     * Get the last iteration on which the specified attribute has been 
     * observed.
     * @param attr The id of the attribute.
     * @return The iteration number.
     */
    public int getAttrLastIteration(T attr)
    {
        Integer lastAttrIteration = recencyMemory.get(attr);
        
        if(lastAttrIteration == null)
            return 0;
        
        return lastAttrIteration;
    }

    /**
     * Gets the current status of the readed frequencies in a Map form.
     * @return a Map with the attributes (keys) and their related frequencies
     *  (values).
     */
    public Map<T, Double> getReadingFrequenciesSnapshot()
    {
        Map<T, Double> res = new HashMap<>(frequencyMemory.size());
        
        for(T attr : frequencyMemory.keySet())
            res.put(attr, getReadingFrequency(attr));
        
        return res;
    }

    /**
     * Gets an iterator for the attributes within this memory.
     * @return The iterator object.
     */
    public Iterator<T> getAttributes()
    {
        return frequencyMemory.keySet().iterator();
    }

    /**
     * Gets the attributes count in memory.
     * @return The attributes count.
     */
    public int getAttributesCount()
    {
        return frequencyMemory.size();
    }
}

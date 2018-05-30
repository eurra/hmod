
package hmod.solvers.hh.models.attr;

import java.util.Iterator;
import java.util.Objects;

/**
 * Defines a collection of attributes readings which could be collected and can
 * be provided to a memory structure to be registered.
 * @author Enrique Urra C.
 * @param <T> The attribute type
 */
public interface AttributesCollection<T> extends Iterable<T>
{
    public static <T, K> AttributesCollection<T> createFrom(K lowLevelSolution, AttributesProcessor<K, T>... processors)
    {
        Objects.requireNonNull(lowLevelSolution, "null low level solution");
        MutableCollection<T> collection = new MutableCollection<>();
        
        for(int i = 0; i < processors.length;i ++)
            Objects.requireNonNull(processors[i], "null processor at position " + i).processAttributes(lowLevelSolution, collection);
        
        return collection;
    }
    
    /**
     * Checks if a particular attribute exists in the collection.
     * @param attrId The id of the attribute.
     * @return true if the attribute exists, false otherwise.
     */
    boolean hasAttribute(T attrId);
    
    /**
     * Gets the attributes count in the collection.
     * @return The attributes count.
     */
    int getAttributesCount();
}

package hmod.solvers.hh.models.oldattr;

import java.util.Iterator;
import java.util.Objects;

public interface AttributesCollection<T> {
    public static <T, K> AttributesCollection<T> createFrom(K lowLevelSolution, AttributesProcessor<K, T> ... processors) {
        Objects.requireNonNull(lowLevelSolution, "null low level solution");
        MutableCollection collection = new MutableCollection();
        for (int i = 0; i < processors.length; ++i) {
            Objects.requireNonNull(processors[i], "null processor at position " + i).processAttributes(lowLevelSolution, collection);
        }
        return collection;
    }

    public boolean hasAttribute(T var1);

    public Iterator<T> getAttributesIterator();

    public int getAttributesCount();
}

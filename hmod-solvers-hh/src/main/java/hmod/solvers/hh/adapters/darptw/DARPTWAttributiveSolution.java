
package hmod.solvers.hh.adapters.darptw;

import hmod.domains.darptw.DARPTWSolution;
import hmod.solvers.hh.models.attr.AttributesCollection;
import hmod.solvers.hh.models.attr.AttributesProcessor;
import hmod.solvers.hh.models.attr.HHAttributiveSolution;

/**
 *
 * @author Enrique Urra C.
 */
public class DARPTWAttributiveSolution<K, T extends DARPTWAttributiveSolution<K, T>> extends DARPTWHHSolution<T> implements HHAttributiveSolution<K, T>
{
    private AttributesCollection<K> collection;
    private final AttributesProcessor<DARPTWSolution, K>[] processors;

    public DARPTWAttributiveSolution(DARPTWSolution innerSolution, AttributesProcessor<DARPTWSolution, K>... processors)
    {
        super(innerSolution);
        this.processors = processors;
    }

    @Override
    public AttributesCollection<K> getAttributesCollection()
    {
        if(collection == null)
            collection = AttributesCollection.createFrom(getInnerSolution(), processors);
        
        return collection;
    }
}

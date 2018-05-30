
package hmod.solvers.hh.adapters.mkp;

import hmod.domains.mkp.MKPSolution;
import hmod.solvers.hh.models.attr.AttributesCollection;
import hmod.solvers.hh.models.attr.AttributesProcessor;
import hmod.solvers.hh.models.attr.HHAttributiveSolution;

/**
 *
 * @author Enrique Urra C.
 */
public class MKPHHAttributiveSolution<K, T extends MKPHHAttributiveSolution<K, T>> extends MKPHHSolution<T> implements HHAttributiveSolution<K, T>
{
    private AttributesCollection<K> collection;
    private final AttributesProcessor<MKPSolution, K>[] processors;

    public MKPHHAttributiveSolution(MKPSolution lowLevelSolution, AttributesProcessor<MKPSolution, K>... processors)
    {
        super(lowLevelSolution);
        this.processors = processors;
    }

    @Override
    public AttributesCollection<K> getAttributesCollection()
    {
        if(collection == null)
            collection = AttributesCollection.createFrom(getInnerSolution(), processors);
        
        return collection;
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}

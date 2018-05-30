
package hmod.solvers.hh.models.attr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public final class AttributesSnapshot
{
    private final Map<Object, Integer> attrMultipliers = new HashMap<>();
    private final int countReference;

    public AttributesSnapshot(Collection<? extends HHAttributiveSolution> solutions)
    {
        this.countReference = (Objects.requireNonNull(solutions, "null solutions").size());
        
        for(HHAttributiveSolution sol : solutions)
        {
            for(Object attr : sol.getAttributesCollection())
            {
                if(attrMultipliers.containsKey(attr))
                    attrMultipliers.put(attr, attrMultipliers.get(attr) + 1);
                else
                    attrMultipliers.put(attr, 1);
            }
        }
    }
    
    public double getAttributeExistenceScore(HHAttributiveSolution sol)
    {
        AttributesCollection candidateAttrs = sol.getAttributesCollection();
        double maxScore = candidateAttrs.getAttributesCount();
        double existenceScore = 0.0;

        for(Object attr : candidateAttrs)
        {
            if(attrMultipliers.containsKey(attr))
            {
                int elitesWithAttr = attrMultipliers.get(attr);
                existenceScore += (double) elitesWithAttr / countReference;
            }
        }

        return existenceScore / maxScore;
    }
}

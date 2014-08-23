
package hmod.loader.graph;

import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import hmod.core.Condition;
import hmod.core.ORCondition;

/**
 *
 * @author Enrique Urra C.
 */
public class ORConditionBuilder extends MultiConditionBuilder
{
    public ORConditionBuilder(NestedBuilder<Condition>... builders)
    {
        super(builders);
    }
    
    @Override
    public Condition getMainCondition(Condition[] baseConds, BuildSession session)
    {
        ORCondition op = new ORCondition();
        op.setEvaluators(baseConds);
        
        return op;
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("ORCondition");
    }
}

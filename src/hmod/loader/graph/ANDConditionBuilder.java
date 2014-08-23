
package hmod.loader.graph;

import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import hmod.core.ANDCondition;
import hmod.core.Condition;

/**
 *
 * @author Enrique Urra C.
 */
class ANDConditionBuilder extends MultiConditionBuilder
{
    public ANDConditionBuilder(NestedBuilder<Condition>... builders)
    {
        super(builders);
    }
    
    @Override
    public Condition getMainCondition(Condition[] baseDeciders, BuildSession session)
    {
        ANDCondition op = new ANDCondition();
        op.setEvaluators(baseDeciders);
        
        return op;
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("ANDCondition");
    }
}

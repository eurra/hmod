
package hmod.loader.graph;

import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import hmod.core.Condition;
import hmod.core.NORCondition;

/**
 *
 * @author Enrique Urra C.
 */
class NOTConditionBuilder extends MultiConditionBuilder
{   
    public NOTConditionBuilder(NestedBuilder<Condition> target)
    {
        super(target);
    }
    
    @Override
    public Condition getMainCondition(Condition[] baseConds, BuildSession session)
    {
        NORCondition op = new NORCondition();
        op.setDecider(baseConds[0]);
        
        return op;
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("NORCondition");
    }
}

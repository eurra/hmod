

package hmod.loader.graph;

import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.DefaultStateInfo;
import hmod.core.Condition;
import hmod.core.ConditionStep;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultConditionStepBuilder extends AbstractBuilder<Step> implements ConditionStepBuilder
{
    private NestedBuilder<? extends Condition> condition;
    private NestedBuilder<Step> trueStep;
    private NestedBuilder<Step> falseStep;
    
    @Override
    public DefaultConditionStepBuilder setCondition(NestedBuilder<? extends Condition> decider)
    {
        this.condition = decider;
        return this;
    }
    
    @Override
    public DefaultConditionStepBuilder setTrueStep(NestedBuilder<Step> trueStep)
    {
        this.trueStep = trueStep;
        return this;
    }

    @Override
    public DefaultConditionStepBuilder setFalseStep(NestedBuilder<Step> falseStep)
    {
        this.falseStep = falseStep;
        return this;
    }

    @Override
    public void buildInstance(BuildSession session) throws BuildException
    {
        ConditionStep result = new ConditionStep();
        session.registerResult(this, result);
        
        result.setCondition(condition == null ? null : condition.build(session));
        result.setNextTrue(trueStep == null ? null : trueStep.build(session));
        result.setNextFalse(falseStep == null ? null : falseStep.build(session));
    }

    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("ConditionStep");
    }
}

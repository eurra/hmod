
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import hmod.core.Condition;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface ConditionStepBuilder extends NestedBuilder<Step>, ConditionStepInput, SingleConditionInput
{
    @Override ConditionStepBuilder setTrueStep(NestedBuilder<Step> trueStep);
    @Override ConditionStepBuilder setFalseStep(NestedBuilder<Step> falseStep);
    @Override ConditionStepBuilder setCondition(NestedBuilder<? extends Condition> condition);
}


package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface ConditionStepInput extends Delegate
{
    ConditionStepInput setTrueStep(NestedBuilder<Step> trueStep);
    ConditionStepInput setFalseStep(NestedBuilder<Step> falseStep);
}


package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import hmod.core.Operator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface SequentialStepBuilder extends NestedBuilder<Step>, DirectStepInput, MultiOperatorInput
{
    @Override SequentialStepBuilder setNextStep(NestedBuilder<Step> next);
    @Override SequentialStepBuilder addOperator(NestedBuilder<? extends Operator> operator);
}


package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;
import hmod.core.BooleanOperator;
import hmod.core.Operator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface BooleanStepBuilder extends Builder, Buildable<Step>, DecisionStepInput, DeciderInput, MultiOperatorInput
{
    @Override BooleanStepBuilder setTrueStep(Buildable<Step> trueStep) throws BuildException;
    @Override BooleanStepBuilder setFalseStep(Buildable<Step> falseStep) throws BuildException;
    @Override BooleanStepBuilder addOperator(Buildable<? extends Operator> operator) throws BuildException;
    @Override BooleanStepBuilder setDecider(Buildable<? extends BooleanOperator> decider) throws BuildException;
}

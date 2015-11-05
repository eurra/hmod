
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;
import hmod.core.BooleanOperator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface ORStepBuilder extends Builder, Buildable<Step>, DecisionStepInput, MultiDeciderInput
{
    @Override ORStepBuilder setTrueStep(Buildable<Step> trueStep) throws BuildException;
    @Override ORStepBuilder setFalseStep(Buildable<Step> falseStep) throws BuildException;
    @Override ORStepBuilder addDecider(Buildable<? extends BooleanOperator> operator) throws BuildException;
}

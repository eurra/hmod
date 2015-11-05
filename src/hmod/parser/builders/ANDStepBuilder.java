
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
public interface ANDStepBuilder extends Builder, Buildable<Step>, DecisionStepInput, MultiDeciderInput
{
    @Override ANDStepBuilder setTrueStep(Buildable<Step> trueStep) throws BuildException;
    @Override ANDStepBuilder setFalseStep(Buildable<Step> falseStep) throws BuildException;
    @Override ANDStepBuilder addDecider(Buildable<? extends BooleanOperator> operator) throws BuildException;
}

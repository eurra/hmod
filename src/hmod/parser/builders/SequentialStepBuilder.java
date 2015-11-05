
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;
import hmod.core.Operator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface SequentialStepBuilder extends Builder, Buildable<Step>, DirectStepInput, MultiOperatorInput
{
    @Override SequentialStepBuilder setNextStep(Buildable<Step> next) throws BuildException;
    @Override SequentialStepBuilder addOperator(Buildable<? extends Operator> operator) throws BuildException;
}

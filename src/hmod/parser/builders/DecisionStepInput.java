
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface DecisionStepInput extends Delegate
{
    DecisionStepInput setTrueStep(Buildable<Step> trueStep) throws BuildException;
    DecisionStepInput setFalseStep(Buildable<Step> falseStep) throws BuildException;
}


package hmod.parser.builders;

import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface DecisionStepOutput extends Delegate
{
    Buildable<Step> getTrueStep();
    Buildable<Step> getFalseStep();
}

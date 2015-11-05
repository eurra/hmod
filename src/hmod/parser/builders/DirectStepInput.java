
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface DirectStepInput extends Delegate
{
    DirectStepInput setNextStep(Buildable<Step> next) throws BuildException;
}


package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public class DirectStepDelegateImpl implements DirectStepInput, DirectStepOutput
{
    private Buildable<Step> nextStep;

    @Override
    public DirectStepInput setNextStep(Buildable<Step> next) throws BuildException
    {
        nextStep = next;
        return this;
    }

    @Override
    public Buildable<Step> getNextStep()
    {
        return nextStep;
    }
}

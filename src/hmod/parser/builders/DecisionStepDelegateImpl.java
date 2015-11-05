
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
class DecisionStepDelegateImpl implements DecisionStepInput, DecisionStepOutput
{
    private Buildable<Step> trueStep;
    private Buildable<Step> falseStep;

    @Override
    public DecisionStepInput setTrueStep(Buildable<Step> trueStep) throws BuildException
    {
        this.trueStep = trueStep;
        return this;
    }

    @Override
    public DecisionStepInput setFalseStep(Buildable<Step> falseStep) throws BuildException
    {
        this.falseStep = falseStep;
        return this;
    }

    @Override
    public Buildable<Step> getTrueStep()
    {
        return trueStep;
    }

    @Override
    public Buildable<Step> getFalseStep()
    {
        return falseStep;
    }
}

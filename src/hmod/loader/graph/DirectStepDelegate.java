
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public class DirectStepDelegate implements DirectStepInput, DirectStepOutput
{
    private NestedBuilder<Step> nextStep;

    @Override
    public DirectStepInput setNextStep(NestedBuilder<Step> next)
    {
        nextStep = next;
        return this;
    }

    @Override
    public NestedBuilder<Step> getNextStep()
    {
        return nextStep;
    }
}


package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface SubProcessStepBuilder extends NestedBuilder<Step>, DirectStepInput, SubProcessStepInput
{
    @Override SubProcessStepBuilder setNextStep(NestedBuilder<Step> next);
    @Override SubProcessStepBuilder setSubStep(NestedBuilder<Step> step);
}

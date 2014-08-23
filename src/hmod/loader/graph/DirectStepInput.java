
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface DirectStepInput extends Delegate
{
    DirectStepInput setNextStep(NestedBuilder<Step> next);
}

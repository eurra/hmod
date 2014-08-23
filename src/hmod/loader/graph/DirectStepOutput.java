
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface DirectStepOutput extends Delegate
{
    NestedBuilder<Step> getNextStep();
}

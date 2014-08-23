
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface ExtensionStepBuilder extends NestedBuilder<Step>, DirectStepInput, ExtensionStepInput
{
    @Override ExtensionStepBuilder setNextStep(NestedBuilder<Step> next);
    @Override ExtensionStepBuilder addFirst(NestedBuilder<Step> extension);
    @Override ExtensionStepBuilder addLast(NestedBuilder<Step> extension);
    @Override ExtensionStepBuilder addAt(NestedBuilder<Step> extension, int pos);
}

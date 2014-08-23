
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface ExtensionStepInput extends Delegate
{
    int getExtensionsCount();
    ExtensionStepInput addFirst(NestedBuilder<Step> extension);
    ExtensionStepInput addLast(NestedBuilder<Step> extension);
    ExtensionStepInput addAt(NestedBuilder<Step> extension, int pos);
}

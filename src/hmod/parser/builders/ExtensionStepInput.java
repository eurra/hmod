
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface ExtensionStepInput extends Delegate
{
    int getExtensionsCount();
    ExtensionStepInput addFirst(Buildable<Step> extension) throws BuildException;
    ExtensionStepInput addLast(Buildable<Step> extension) throws BuildException;
    ExtensionStepInput addAt(Buildable<Step> extension, int pos) throws BuildException;
}


package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface ExtensionStepBuilder extends Builder, Buildable<Step>, DirectStepInput, ExtensionStepInput
{
    @Override ExtensionStepBuilder setNextStep(Buildable<Step> next) throws BuildException;
    @Override ExtensionStepBuilder addFirst(Buildable<Step> extension) throws BuildException;
    @Override ExtensionStepBuilder addLast(Buildable<Step> extension) throws BuildException;
    @Override ExtensionStepBuilder addAt(Buildable<Step> extension, int pos) throws BuildException;
}

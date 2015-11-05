
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.BooleanOperator;

/**
 *
 * @author Enrique Urra C.
 */
public interface DeciderInput extends Delegate
{
    DeciderInput setDecider(Buildable<? extends BooleanOperator> decider) throws BuildException;
}

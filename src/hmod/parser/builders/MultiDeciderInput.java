
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.BooleanOperator;

/**
 *
 * @author Enrique Urra C.
 */
public interface MultiDeciderInput extends Delegate
{
    MultiDeciderInput addDecider(Buildable<? extends BooleanOperator> decider) throws BuildException;
}

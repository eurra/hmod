
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
public interface SingleOperatorInput extends Delegate
{
    SingleOperatorInput setOperator(Buildable<? extends Operator> operator) throws BuildException;
}

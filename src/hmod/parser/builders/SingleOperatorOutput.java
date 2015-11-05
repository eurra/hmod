
package hmod.parser.builders;

import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
public interface SingleOperatorOutput extends Delegate
{
    Buildable<Operator> getOperator();
}

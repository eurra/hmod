
package hmod.parser.builders;

import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.BooleanOperator;

/**
 *
 * @author Enrique Urra C.
 */
public interface DeciderOutput extends Delegate
{
    Buildable<BooleanOperator> getDecider();
}

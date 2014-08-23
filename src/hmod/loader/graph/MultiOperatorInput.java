
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
public interface MultiOperatorInput extends Delegate
{
    MultiOperatorInput addOperator(NestedBuilder<? extends Operator> operator);
}

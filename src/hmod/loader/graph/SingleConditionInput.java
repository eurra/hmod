
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import hmod.core.Condition;

/**
 *
 * @author Enrique Urra C.
 */
public interface SingleConditionInput extends Delegate
{
    SingleConditionInput setCondition(NestedBuilder<? extends Condition> condition);
}

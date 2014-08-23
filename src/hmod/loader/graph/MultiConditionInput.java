
package hmod.loader.graph;

import flexbuilders.core.NestedBuilder;
import flexbuilders.core.Delegate;
import hmod.core.Condition;

/**
 *
 * @author Enrique Urra C.
 */
public interface MultiConditionInput<T> extends Delegate
{
    MultiConditionInput<T> addCondition(NestedBuilder<? extends Condition> condition);
}

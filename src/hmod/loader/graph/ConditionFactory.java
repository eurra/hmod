
package hmod.loader.graph;

import hmod.core.Condition;
import java.util.function.Function;

/**
 *
 * @author Enrique Urra C.
 */
public interface ConditionFactory extends Function<ArgumentList, Condition>
{
}

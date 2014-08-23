
package hmod.loader.graph;

import hmod.core.Operator;
import java.util.function.Function;

/**
 *
 * @author Enrique Urra C.
 */
public interface OperatorFactory extends Function<ArgumentList, Operator>
{
}


package hmod.parser.builders;

import flexbuilders.basic.Setter;
import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;
import hmod.core.DataInterface;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
public interface OperatorBuilder<T extends Operator> extends Builder, Buildable<T>, OperatorInput
{
    @Override OperatorBuilder<T> injectData(Buildable<? extends DataInterface> data) throws BuildException;
    @Override OperatorBuilder<T> injectData(Buildable<? extends Setter> setter, Buildable<? extends DataInterface> dataImpl) throws BuildException;
}

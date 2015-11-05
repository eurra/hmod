
package hmod.parser.builders;

import flexbuilders.basic.Setter;
import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.DataInterface;

/**
 *
 * @author Enrique Urra C.
 */
public interface OperatorInput extends Delegate
{
    OperatorInput injectData(Buildable<? extends DataInterface> data) throws BuildException;
    OperatorInput injectData(Buildable<? extends Setter> setter, Buildable<? extends DataInterface> dataImpl) throws BuildException;
}

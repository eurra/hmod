
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Delegate;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
public interface MultiOperatorOutput extends Delegate
{
    Operator[] getOperators() throws BuildException;
}

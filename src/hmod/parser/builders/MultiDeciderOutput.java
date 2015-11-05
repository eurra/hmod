
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Delegate;
import hmod.core.BooleanOperator;

/**
 *
 * @author Enrique Urra C.
 */
public interface MultiDeciderOutput extends Delegate
{
    BooleanOperator[] getDeciders() throws BuildException;
}

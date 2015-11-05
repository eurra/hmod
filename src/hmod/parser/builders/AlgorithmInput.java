
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmInput extends Delegate
{
    AlgorithmInput startWith(Buildable<Step> start) throws BuildException;
}


package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Builder;
import hmod.core.Algorithm;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmBuilder extends Builder, Buildable<Algorithm>, AlgorithmInput
{
    @Override AlgorithmBuilder startWith(Buildable<Step> start) throws BuildException;
}

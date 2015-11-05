
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.Delegate;
import hmod.core.Algorithm;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public interface SubProcessStepInput extends Delegate
{
    SubProcessStepInput setSubAlgorithm(Buildable<Algorithm> algorithm) throws BuildException;
    SubProcessStepInput setSubStep(Buildable<Step> step) throws BuildException;
}

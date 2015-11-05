
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
public interface SubProcessStepBuilder extends Builder, Buildable<Step>, DirectStepInput, SubProcessStepInput
{
    @Override SubProcessStepBuilder setNextStep(Buildable<Step> next) throws BuildException;
    @Override SubProcessStepBuilder setSubAlgorithm(Buildable<Algorithm> algorithm) throws BuildException;
    @Override SubProcessStepBuilder setSubStep(Buildable<Step> step) throws BuildException;
}

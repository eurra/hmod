
package hmod.domains.launcher.graph;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.array;
import flexbuilders.core.BuildException;
import flexbuilders.core.BuildSession;
import static flexbuilders.core.BuildSession.newSesion;
import flexbuilders.core.NestedBuilder;
import hmod.core.Algorithm;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public final class RunSession implements NestedBuilder<Algorithm>
{
    private final ArrayBuilder<Step> steps;

    public RunSession()
    {
        this.steps = array(Step.class);
    }
    
    public void addStep(NestedBuilder<Step> step) throws BuildException
    {
        steps.elem(step);
    }

    @Override
    public Algorithm build() throws BuildException
    {
        return build(newSesion());
    }

    @Override
    public Algorithm build(BuildSession session) throws BuildException
    {
        Step[] finalSteps = steps.asArray().build(session);
        return Algorithm.create(finalSteps);
    }
}

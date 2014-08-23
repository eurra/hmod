
package hmod.loader.graph;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.ProxyDelegate;
import flexbuilders.core.AbstractBuilder;
import flexbuilders.core.BuildSession;
import flexbuilders.core.StateBuilder;
import hmod.core.DirectStep;
import hmod.core.Operator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class DirectStepBuilder extends ProxyDelegate implements NestedBuilder<Step>, StateBuilder
{    
    private class InnerBuildable extends AbstractBuilder<Step>
    {
        @Override
        public void buildInstance(BuildSession session) throws BuildException
        {
            DirectStep step = new DirectStep();
            session.registerResult(this, step);
            
            Operator op = createOperator(session);

            if(op == null)
                throw new BuildException("The provided operator cannot be null");

            NestedBuilder<Step> nextStepBuildable = getProxyAs(DirectStepOutput.class).getNextStep();
            step.setOperator(op);
            step.setNextStep(nextStepBuildable != null ? nextStepBuildable.build(session) : null);
        }

        @Override
        public BuildStateInfo getStateInfo()
        {
            return DirectStepBuilder.this.getStateInfo();
        }
    }
    
    private final InnerBuildable innerBuildable = new InnerBuildable();

    @Override
    public final Step build()
    {
        return innerBuildable.build();
    }

    @Override
    public Step build(BuildSession session) throws BuildException
    {
        return innerBuildable.build(session);
    }

    @Override
    public String toString()
    {
        return innerBuildable.toString();
    }
    
    public abstract Operator createOperator(BuildSession session);
}
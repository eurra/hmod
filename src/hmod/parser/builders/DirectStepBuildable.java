
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.ProxyDelegate;
import flexbuilders.core.StackBuildable;
import hmod.core.AlgorithmException;
import hmod.core.Operator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class DirectStepBuildable extends ProxyDelegate implements Buildable<Step>
{
    private class DirectStep implements Step
    {
        private Operator directOperator;
        private Step next;

        @Override
        public Step resolveNext() throws AlgorithmException
        {
            directOperator.execute();
            return next;
        }
    }
    
    private class InnerBuildable extends StackBuildable<Step>
    {
        private DirectStep step;

        @Override
        public void buildInstance() throws BuildException
        {
            step = new DirectStep();
            Operator directOperator = getDirectOperator();

            if(directOperator == null)
                throw new BuildException("The provided direct operator cannot be null");

            Buildable<Step> nextStepBuildable = getProxyAs(DirectStepOutput.class).getNextStep();
            step.directOperator = directOperator;
            step.next = nextStepBuildable != null ? nextStepBuildable.build() : null;
        }

        @Override
        public Step getInstance() throws BuildException
        {
            return step;
        }

        @Override
        public BuildStateInfo getStateInfo()
        {
            return DirectStepBuildable.this.getStateInfo();
        }
    }
    
    private InnerBuildable innerBuildable = new InnerBuildable();

    @Override
    public final Step build() throws BuildException
    {
        return innerBuildable.build();
    }

    @Override
    public String toString()
    {
        return innerBuildable.toString();
    }
    
    public abstract BuildStateInfo getStateInfo();
    public abstract Operator getDirectOperator() throws BuildException;
}
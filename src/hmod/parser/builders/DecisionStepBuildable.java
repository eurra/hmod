
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.ProxyDelegate;
import flexbuilders.core.StackBuildable;
import hmod.core.AlgorithmException;
import hmod.core.BooleanOperator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class DecisionStepBuildable extends ProxyDelegate implements Buildable<Step>
{
    private class DecisionStep implements Step
    {
        private BooleanOperator deciderOperator;
        private Step nextTrue;
        private Step nextFalse;

        @Override
        public Step resolveNext() throws AlgorithmException
        {
            if(deciderOperator.evaluate())
                return nextTrue;
            else
                return nextFalse;
        }
    }
    
    private class InnerBuildable extends StackBuildable<Step>
    {
        private DecisionStep step;

        @Override
        public void buildInstance() throws BuildException
        {
            step = new DecisionStep();
            BooleanOperator decider = getDeciderOperator();

            if(decider == null)
                throw new BuildException("The provided decider cannot be null");

            DecisionStepOutput output = getProxyAs(DecisionStepOutput.class);
            Buildable<Step> nextTrue = output.getTrueStep();
            Buildable<Step> nextFalse = output.getFalseStep();
            step.deciderOperator = decider;
            step.nextTrue = nextTrue != null ? nextTrue.build() : null;
            step.nextFalse = nextFalse != null ? nextFalse.build() : null;
        }

        @Override
        public Step getInstance() throws BuildException
        {
            return step;
        }

        @Override
        public BuildStateInfo getStateInfo()
        {
            return DecisionStepBuildable.this.getBuildableInfo();
        }
    }

    private InnerBuildable innerBuildable = new InnerBuildable();
    
    @Override
    public final Step build() throws BuildException
    {
        return innerBuildable.build();
    }
    
    public abstract BuildStateInfo getBuildableInfo();
    public abstract BooleanOperator getDeciderOperator() throws BuildException;
}

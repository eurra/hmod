

package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.DefaultStateInfo;
import hmod.core.AlgorithmException;
import hmod.core.BooleanOperator;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
class BooleanStepBuildableImpl extends DecisionStepBuildable
{
    private class BooleanDeciderOperator extends BooleanOperator
    {
        private BooleanOperator decider;
        private Operator[] operators;

        @Override
        public Boolean evaluate() throws AlgorithmException
        {
            if(operators != null)
            {
                for(int i = 0; i < operators.length; i++)
                    operators[i].execute();
            }
            
            return decider.evaluate();
        }
    }

    @Override
    public BooleanOperator getDeciderOperator() throws BuildException
    {
        BooleanDeciderOperator decider = new BooleanDeciderOperator();
        Buildable<BooleanOperator> innerDecider = getProxyAs(DeciderOutput.class).getDecider();
        
        if(innerDecider == null)
            throw new BuildException("The decider operator has not been set");
        
        Operator[] operators = getProxyAs(MultiOperatorOutput.class).getOperators();        
        decider.decider = innerDecider.build();
        decider.operators = operators;
        
        return decider;
    }
    
    @Override
    public BuildStateInfo getBuildableInfo()
    {
        return new DefaultStateInfo().
            setName("BooleanStep");
    }
}

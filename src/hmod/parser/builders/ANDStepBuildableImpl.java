
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import hmod.core.AlgorithmException;
import hmod.core.BooleanOperator;

/**
 *
 * @author Enrique Urra C.
 */
class ANDStepBuildableImpl extends DecisionStepBuildable
{
    private class ANDDeciderOperator extends BooleanOperator
    {
        private BooleanOperator[] operators;

        @Override
        public Boolean evaluate() throws AlgorithmException
        {
            if(operators != null)
            {
                for(int i = 0; i < operators.length; i++)
                {
                    if(operators[i] != null && !operators[i].evaluate())
                        return false;
                }
            }
            
            return true;
        }
    }
    
    @Override
    public BooleanOperator getDeciderOperator() throws BuildException
    {
        ANDDeciderOperator decider = new ANDDeciderOperator();
        decider.operators = getProxyAs(MultiDeciderOutput.class).getDeciders();
        
        return decider;
    }
    
    @Override
    public BuildStateInfo getBuildableInfo()
    {
        return new DefaultStateInfo().
            setName("ANDStep");
    }
}

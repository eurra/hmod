
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
public class ORStepBuildableImpl extends DecisionStepBuildable
{
    private class ORDeciderOperator extends BooleanOperator
    {
        private BooleanOperator[] operators;

        @Override
        public Boolean evaluate() throws AlgorithmException
        {
            if(operators != null)
            {
                for(int i = 0; i < operators.length; i++)
                {
                    if(operators[i].evaluate())
                        return true;
                }
            }
            
            return false;
        }
    }

    @Override
    public BooleanOperator getDeciderOperator() throws BuildException
    {
        ORDeciderOperator decider = new ORDeciderOperator();        
        decider.operators = getProxyAs(MultiDeciderOutput.class).getDeciders();
        
        return decider;
    }
    
    @Override
    public BuildStateInfo getBuildableInfo()
    {
        return new DefaultStateInfo().
            setName("ORStep");
    }
}

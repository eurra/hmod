
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.DefaultStateInfo;
import hmod.core.AlgorithmException;
import hmod.core.Operator;

/**
 *
 * @author Enrique Urra C.
 */
class SequentialStepBuildableImpl extends DirectStepBuildable
{
    private class SequentialStepOperator implements Operator
    {
        private Operator[] operators;

        @Override
        public void execute() throws AlgorithmException
        {
            if(operators != null)
            {
                for(int i = 0; i < operators.length; i++)
                    operators[i].execute();
            }
        }
    }

    @Override
    public Operator getDirectOperator() throws BuildException
    {
        SequentialStepOperator op = new SequentialStepOperator();
        op.operators = getProxyAs(MultiOperatorOutput.class).getOperators();
        
        return op;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("SequentialStep");
    }
}

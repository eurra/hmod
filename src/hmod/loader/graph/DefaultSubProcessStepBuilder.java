
package hmod.loader.graph;

import flexbuilders.core.BuildSession;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.NestedBuilder;
import flexbuilders.core.BuildStateInfo;
import hmod.core.Algorithm;
import hmod.core.AlgorithmException;
import hmod.core.Operator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultSubProcessStepBuilder extends DirectStepBuilder implements SubProcessStepInput
{
    private class SubProcessOperator implements Operator
    {
        private Algorithm alg;

        @Override
        public void execute() throws AlgorithmException
        {
            if(alg != null)
                alg.start();
        }
    }

    private NestedBuilder<Step> subProcess;

    @Override
    public SubProcessStepInput setSubStep(NestedBuilder<Step> step)
    {
        subProcess = step;
        return this;
    }
    
    @Override
    public Operator createOperator(BuildSession session)
    {
        SubProcessOperator op = new SubProcessOperator();
        
        if(subProcess != null)
            op.alg = Algorithm.create(subProcess.build(session));
        
        return op;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("SubProcessStep");
    }
}


package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.BuildStateInfo;
import hmod.core.Algorithm;
import hmod.core.AlgorithmException;
import hmod.core.Operator;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
class SubProcessStepDelegateImpl extends DirectStepBuildable implements SubProcessStepInput
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

    private Buildable<Algorithm> subProcess;

    @Override
    public SubProcessStepInput setSubAlgorithm(Buildable<Algorithm> algorithm) throws BuildException
    {
        subProcess = algorithm;
        return this;
    }

    @Override
    public SubProcessStepInput setSubStep(Buildable<Step> step) throws BuildException
    {
        subProcess = AlgorithmBuilders.algorithm().startWith(step);
        return this;
    }
    
    @Override
    public Operator getDirectOperator() throws BuildException
    {
        SubProcessOperator op = new SubProcessOperator();
        
        if(subProcess != null)
            op.alg = subProcess.build();
        
        return op;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("SubProcessStep");
    }
}

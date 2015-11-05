
package hmod.parser.builders;

import flexbuilders.core.BuildException;
import flexbuilders.core.DefaultStateInfo;
import flexbuilders.core.Buildable;
import flexbuilders.core.BuildStateInfo;
import flexbuilders.core.StackBuildable;
import hmod.core.Algorithm;
import hmod.core.AlgorithmException;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
class AlgorithmBuilderImpl extends StackBuildable<Algorithm> implements AlgorithmBuilder
{
    private static class InnerAlgorithm implements Algorithm
    {
        private Step startStep;
        private boolean stopped;
        
        @Override
        public void start() throws AlgorithmException
        {
            Step currStep = startStep;

            while(currStep != null && !stopped)
                currStep = currStep.resolveNext();
        }

        @Override
        public void stop() throws AlgorithmException
        {
            stopped = true;
        }
    }
    
    private InnerAlgorithm algorithm;
    private Buildable<Step> startStep;

    @Override
    public AlgorithmBuilderImpl startWith(Buildable<Step> start) throws BuildException
    {
        this.startStep = start;
        return this;
    }

    @Override
    public void buildInstance() throws BuildException
    {
        algorithm = new InnerAlgorithm();
        algorithm.startStep = startStep == null ? null : startStep.build();
    }

    @Override
    public Algorithm getInstance() throws BuildException
    {
        return algorithm;
    }
    
    @Override
    public BuildStateInfo getStateInfo()
    {
        return new DefaultStateInfo().
            setName("Algorithm");
    }
}


package hmod.solvers.hh.models.basicops;

import hmod.core.Block;
import hmod.core.Statement;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.common.MutableIterationHandler;
import hmod.solvers.hh.models.selection.LLHeuristicsHandler;
import hmod.solvers.hh.models.selection.HeuristicRunnerHandler;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
class RandomPermutationProcesses
{
    private final LLHeuristicsHandler heuristicData;
    private final HeuristicPermutationHandler permData;
    private final HeuristicRunnerHandler runnerHandler;

    public RandomPermutationProcesses(LLHeuristicsHandler heuristicData, 
                                      HeuristicPermutationHandler heuristicPermData,
                                      HeuristicRunnerHandler runnerHandler)
    {
        this.heuristicData = heuristicData;
        this.permData = heuristicPermData;
        this.runnerHandler = runnerHandler;
    }
    
    public void storeRandomPermutation()
    {
        int heuristicCount = heuristicData.getHeuristicsCount();
        Statement[] heuristics = new Block[heuristicData.getHeuristicsCount()];

        for(int i = 0; i < heuristicCount; i++)
            heuristics[i] = heuristicData.getHeuristicAt(i);

        Statement[] randomPerm = RandomTool.fastArrayShuffle(heuristics);
        permData.storePermutation(randomPerm);
    }
    
    public Statement initializePermutationIteration(MutableIterationHandler iterationData)
    {
        return () -> {
            int permSize = permData.getSizeOfPermutation();
            iterationData.setMaxIterations(permSize);
        };
    }
    
    public Statement nextHeuristicInPermutation(IterationHandler iterationData)
    {
        return () -> {
            int currIndex = iterationData.getCurrentIteration();
            Statement currHeuristic = permData.getHeuristicFromPermutation(currIndex);
            runnerHandler.setHeuristicToRun(currHeuristic);
        };
    }
}

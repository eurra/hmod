
package hmod.solvers.hh.models.basicops;

import hmod.core.Statement;
import hmod.solvers.common.MutableIterationHandler;
import hmod.solvers.hh.HHSolution;
import hmod.solvers.hh.models.selection.LLHeuristicsHandler;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;

/**
 *
 * @author Enrique Urra C.
 */
class GreedyProcesses
{
    private final GreedyHandler greedyHandler;
    private final LLHeuristicsHandler heuristicHandler;
    private final TrackableSolutionHandler solutionData;
    private final HeuristicSelectionHandler hsData;

    public GreedyProcesses(GreedyHandler greedyHandler, 
                           LLHeuristicsHandler heuristicHandler, 
                           TrackableSolutionHandler solutionData, 
                           HeuristicSelectionHandler hsData)
    {
        this.greedyHandler = greedyHandler;
        this.heuristicHandler = heuristicHandler;
        this.solutionData = solutionData;
        this.hsData = hsData;
    }
    
    public Statement initGreedyIteration(MutableIterationHandler iterationHandler)
    {
        return () -> {
            int heuristicCount = heuristicHandler.getHeuristicsCount();
            greedyHandler.resetGreedyData();
            iterationHandler.setMaxIterations(heuristicCount);
        };
    }
    
    public void registerSolutionFromOutput()
    {
        HHSolution outputSol = solutionData.getOutputSolution();
        greedyHandler.registerSolution(outputSol);
    }
    
    public void storeBestAsProcessed()
    {
        HHSolution bestSol = greedyHandler.getBestSolution();
        hsData.storeProcessedSolution(bestSol);
    }
}

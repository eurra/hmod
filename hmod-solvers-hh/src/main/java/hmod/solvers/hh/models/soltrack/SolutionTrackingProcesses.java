
package hmod.solvers.hh.models.soltrack;

import hmod.core.Condition;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.hh.HHSolution;
import hmod.solvers.hh.models.selection.SelectionHHeuristicOutputIds;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class SolutionTrackingProcesses
{
    private final TrackableSolutionHandler sh;
    private final SolutionTrackingHandler sth;
    private final IterationHandler ih;

    SolutionTrackingProcesses(TrackableSolutionHandler sh,
                              SolutionTrackingHandler sth,
                              IterationHandler ih)
    {
        this.sh = sh;
        this.ih = ih;
        this.sth = sth;
    }
    
    public Condition areIterationsPassedSinceGlobalImprove(int numIterations)
    {
        return () -> sth.getGlobalNoImproveIterations() >= numIterations;
    }
    
    public Condition areIterationsPassedSinceLocalImprove(int numIterations)
    {
        return () -> sth.getLocalNoImproveIterations() >= numIterations;
    }
    
    public void printOutputSolutionIfIsBest()
    {
        if(sh.outputImprovedBest())
        {
            HHSolution outputSolution = sh.getOutputSolution();
            
            OutputManager.println(SelectionHHeuristicOutputIds.NEW_BEST_FITNESS, 
                "New best solution (it. " + ih.getCurrentIteration()+ "): " + outputSolution.evaluationToString()
            );
            
            OutputManager.println(SelectionHHeuristicOutputIds.NEW_BEST_FITNESS_SHEET, 
                ih.getCurrentIteration()+ "\t" + outputSolution.getEvaluation()
            );
        }
    }
}

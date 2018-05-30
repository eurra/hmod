
package hmod.solvers.hh.models.basicops;

import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.TimeElapsedHandler;
import hmod.solvers.hh.HHSolution;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class GenericSelectionHHeuristicProcesses
{
    private final TimeElapsedHandler timeElapsedData;
    private final TrackableSolutionHandler solutionData;
    private final HeuristicSelectionHandler hsData;
    private final MoveAcceptanceHandler maData;

    GenericSelectionHHeuristicProcesses(TimeElapsedHandler timeElapsedData, 
                                        TrackableSolutionHandler solutionData, 
                                        HeuristicSelectionHandler hsData, 
                                        MoveAcceptanceHandler maData)
    {
        this.timeElapsedData = timeElapsedData;
        this.solutionData = solutionData;
        this.hsData = hsData;
        this.maData = maData;
    }
    
    public void printEndInfo()
    {
        HHSolution best = solutionData.getBestSolution();
        double totalSecs = timeElapsedData.getElapsedSeconds();

        OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Best result (fitness): " + best.evaluationToString());
        OutputManager.println(GenericSelectionHHeuristicOutputIds.RESULT_SHEET, best.getEvaluation() + "\t" + totalSecs);
    }
    
    public void storeAcceptedSolutionAsToProcess()
    {
        HHSolution accepted = maData.retrieveCurrentlyAccepted();
        hsData.storeSolutionToProcess(accepted);
    }
    
    public void storeProcessedSolutionAsCandidate()
    {
        HHSolution processed = hsData.retrieveSolutionProcessed();
        maData.storeCandidateSolution(processed);
    }
    
    void storeOutputSolutionAsToProcess()
    {
        HHSolution output = solutionData.getOutputSolution();
        hsData.storeSolutionToProcess(output);
    }
    
    /*public void acceptOutputSolution()
    {
        HHSolution output = solutionData.getOutputSolution();
        maData.storeCandidateSolution(output);
        maData.acceptSolution();
    }*/
}

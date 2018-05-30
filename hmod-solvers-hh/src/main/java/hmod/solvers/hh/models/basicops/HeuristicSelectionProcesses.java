
package hmod.solvers.hh.models.basicops;

import hmod.core.OperatorInfo;
import hmod.core.Statement;
import hmod.solvers.hh.HHSolution;
import hmod.solvers.hh.models.selection.LLHeuristicsHandler;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import hmod.solvers.hh.models.selection.HeuristicRunnerHandler;
import java.io.PrintWriter;
import optefx.util.metadata.MetadataManager;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public final class HeuristicSelectionProcesses
{
    public static Statement getHeuristicSelectionBlock(Statement hsBlock,
                                                       String name,
                                                       String description)
    {
        return MetadataManager.getInstance().attachData(
            hsBlock, 
            new OperatorInfo.OperatorInfoBuilder().
                category(HyperheuristicOperator.HEURISTIC_SELECTION).
                name(name).
                description(description).
                build()
        );
    }
    
    private final HeuristicSelectionHandler hsData;
    private final TrackableSolutionHandler solutionData;
    private final LLHeuristicsHandler heuristicData;
    private final HeuristicRunnerHandler runnerData;

    HeuristicSelectionProcesses(HeuristicSelectionHandler hsData, 
                                TrackableSolutionHandler solutionData, 
                                LLHeuristicsHandler heuristicData,
                                HeuristicRunnerHandler runnerData)
    {
        this.hsData = hsData;
        this.solutionData = solutionData;
        this.heuristicData = heuristicData;
        this.runnerData = runnerData;
    }
    
    public void setSolutionToProcessAsInput()
    {
        HHSolution toProcess = hsData.retrieveSolutionToProcess();
        solutionData.setInputSolution(toProcess);
    }
    
    public void storeOutputAsProcessed()
    {
        HHSolution processed = solutionData.getOutputSolution();
        hsData.storeProcessedSolution(processed);
        
        PrintWriter warnings = OutputManager.getCurrent().getOutput(GenericSelectionHHeuristicOutputIds.WARNINGS);
        
        if(warnings != null && solutionData.getInputSolution() != null && solutionData.getInputSolution().isSameTo(solutionData.getOutputSolution()))
            warnings.println("WARNING (HH level): Output solution is the same as input solution");
    }
    
    public boolean checkOutputImprovedToProcess()
    {
        HHSolution outputSol = solutionData.getOutputSolution();
        HHSolution toProcessSol = hsData.retrieveSolutionToProcess();

        return outputSol != null && outputSol.compareTo(toProcessSol) > 0;
    }
    
    public void selectRandomHeuristic()
    {
        int heuristicsCount = heuristicData.getHeuristicsCount();
        Statement randomSelected = heuristicData.getHeuristicAt(RandomTool.getInt(heuristicsCount));
        runnerData.setHeuristicToRun(randomSelected);
    }
    
    public boolean solutionNotChanged()
    {
        return solutionData.getOutputSolution().isSameTo(solutionData.getInputSolution());
    }
}

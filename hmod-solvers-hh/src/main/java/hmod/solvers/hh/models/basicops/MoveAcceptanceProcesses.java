
package hmod.solvers.hh.models.basicops;

import hmod.core.OperatorInfo;
import hmod.core.Statement;
import hmod.solvers.hh.HHSolution;
import optefx.util.metadata.MetadataManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class MoveAcceptanceProcesses
{    
    private MoveAcceptanceHandler acceptanceHandler;

    MoveAcceptanceProcesses(MoveAcceptanceHandler acceptanceHandler)
    {
        this.acceptanceHandler = acceptanceHandler;
    }
    
    public <T extends HHSolution> Statement getMoveAcceptanceBlock(AcceptanceEvaluator<T> evaluator,
                                                                   String name,
                                                                   String description)
    {
        return MetadataManager.getInstance().attachData(
            checkAcceptance(evaluator),
            new OperatorInfo.OperatorInfoBuilder().
            category(HyperheuristicOperator.MOVE_ACCEPTANCE).
            name(name).
            description(description).
            build()
        );
    }
    
    private Statement checkAcceptance(AcceptanceEvaluator acceptanceEvaluator)
    {
        return () -> {
            HHSolution currAcceptedSolution = acceptanceHandler.retrieveCurrentlyAccepted();
            HHSolution currCandidateSolution = acceptanceHandler.retrieveCandidateSolution();
            HHSolution selected;
            
            if(currAcceptedSolution == null)
                selected = currCandidateSolution;
            else if(currCandidateSolution == null)
                selected = currAcceptedSolution;
            else
                selected = acceptanceEvaluator.evaluateAcceptance(currAcceptedSolution, currCandidateSolution);
            
            acceptanceHandler.acceptSolution(selected);
        };
    }

    private MoveAcceptanceProcesses()
    {
    }
}

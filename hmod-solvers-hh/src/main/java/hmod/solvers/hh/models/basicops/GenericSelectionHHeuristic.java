
package hmod.solvers.hh.models.basicops;
import static hmod.core.FlowchartFactory.block;
import hmod.core.OperatorInfo;
import hmod.core.PlaceholderStatement;
import hmod.core.Routine;
import hmod.core.Statement;
import hmod.core.ValidableRoutine;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.common.TimeElapsedHandler;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.loader.Processable;
import optefx.util.metadata.MetadataManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class GenericSelectionHHeuristic
{
    public static final Parameter<HeuristicSelection> HEURISTIC_SELECTION = new Parameter<>("GenericSelectionHHeuristic.HEURISTIC_SELECTION");
    public static final Parameter<MoveAcceptance> MOVE_ACCEPTANCE  = new Parameter<>("GenericSelectionHHeuristic.MOVE_ACCEPTANCE");
    
    @LoadsComponent({
        IterativeHeuristic.class,
        GenericSelectionHHeuristic.class,
        GenericSelectionHHeuristicProcesses.class
    })
    public static void load(ComponentRegister cr,
                            ParameterRegister pr,
                            IterativeHeuristic ih,
                            HeuristicSelectionProcesses hsp,
                            TimeElapsedHandler teh,
                            TrackableSolutionHandler tsh,
                            HeuristicSelectionHandler hsh,
                            MoveAcceptanceHandler mah)
    {
        PlaceholderStatement<Statement> hsBlock = new PlaceholderStatement<>();
        PlaceholderStatement<Statement> maBlock = new PlaceholderStatement<>();
        OperatorInfoHandler oih = new OperatorInfoHandler();
        HeuristicSelection hs = pr.getRequiredValue(HEURISTIC_SELECTION);
        MoveAcceptance ma = pr.getRequiredValue(MOVE_ACCEPTANCE);
        
        pr.addBoundHandler(hs, (v) -> { 
            hsBlock.set(v);
            oih.setHSInfo(MetadataManager.getInstance().getDataFor(v, OperatorInfo.class));
        });
        
        pr.addBoundHandler(ma, (v) -> { 
            maBlock.set(v);
            oih.setMAInfo(MetadataManager.getInstance().getDataFor(v, OperatorInfo.class));
        });
        
        GenericSelectionHHeuristic gshh = cr.provide(new GenericSelectionHHeuristic(hsBlock, maBlock));
        GenericSelectionHHeuristicProcesses gshhp = cr.provide(
            new GenericSelectionHHeuristicProcesses(teh, tsh, hsh, mah)
        );
        
        ih.initReporting().append(oih::printInfo);
        ih.finishReporting().append(gshhp::printEndInfo);
        
        /*ih.initBlock().append(block(
            gshhp::acceptOutputSolution
            hsp::storeOutputAsProcessed,
            gshhp::storeProcessedSolutionAsCandidate,
            mah::acceptCandidate,
            gshhp::storeAcceptedSolutionAsToProcess
        ));*/
        
        ih.init().append(gshhp::storeOutputSolutionAsToProcess);
        
        ih.iteration().append(block(
            gshh.heuristicSelection,
            gshhp::storeProcessedSolutionAsCandidate,
            gshh.moveAcceptance,
            gshhp::storeAcceptedSolutionAsToProcess
        ));
    }
    
    private final ValidableRoutine heuristicSelection = new ValidableRoutine("GenericSelectionHHeuristic.heuristicSelection");
    private final ValidableRoutine moveAcceptance = new ValidableRoutine("GenericSelectionHHeuristic.moveAcceptance");

    private GenericSelectionHHeuristic(Statement hsBlock,
                                       Statement maBlock)
    {
        heuristicSelection.append(hsBlock);
        moveAcceptance.append(maBlock);
    }

    public Routine heuristicSelection() { return heuristicSelection; }
    public Routine moveAcceptance() { return moveAcceptance; }
    
    @Processable
    private void validate()
    {
        heuristicSelection.validate();
        moveAcceptance.validate();
    }
}

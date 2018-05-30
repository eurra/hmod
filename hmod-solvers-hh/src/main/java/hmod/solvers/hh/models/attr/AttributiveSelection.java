
package hmod.solvers.hh.models.attr;

import static hmod.core.FlowchartFactory.*;
import hmod.core.Statement;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.hh.models.oscillation.OscillationHandler;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.common.RouletteSelector;
import hmod.solvers.hh.models.basicops.HeuristicSelection;
import hmod.solvers.hh.models.basicops.HeuristicSelectionProcesses;
import hmod.solvers.hh.models.selection.DomainBarrier;
import hmod.solvers.hh.models.selection.HeuristicRunnerHandler;
import hmod.solvers.hh.models.selection.LLHeuristicsHandler;
import hmod.solvers.hh.models.soltrack.SolutionTrackingHandler;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import java.util.Comparator;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.loader.Resolvable;

/**
 *
 * @author Enrique Urra C.
 */
public final class AttributiveSelection
{   
    public static final HeuristicSelection ATTRIBUTIVE_SELECTION = Resolvable.boundTo(HeuristicSelection.class, 
        AttributiveSelection.class, 
        (ah) -> ah.selectionBlock
    );
    
    public static final Parameter<Double> AMPLIFICATION_FACTOR = new Parameter<>("AttributiveSelection.AMPLIFICATION_FACTOR");
    
    @LoadsComponent(AttributiveSelection.class)
    public static void load(ComponentRegister cr,
                            ParameterRegister pr,
                            DomainBarrier db,
                            OscillationHandler oh,
                            LLHeuristicsHandler llhh,
                            HeuristicRunnerHandler hrh, 
                            TrackableSolutionHandler sh,
                            HeuristicSelectionProcesses hsp,
                            IterationHandler ih,
                            SolutionTrackingHandler sth,
                            IterativeHeuristic iHeu)
    {        
        double amplFactor = pr.getRequiredValue(AMPLIFICATION_FACTOR);
        HeuristicScoringHandler hsh = new HeuristicScoringHandler(llhh);
        AttributiveSelectionHandler ash = new AttributiveSelectionHandler(oh, hrh, hsh, sh, llhh, sth, ih);
        cr.provide(new AttributiveSelection(hsp, db, ash, llhh, sh, amplFactor));
        
        iHeu.finish().append(ash::printHeuristicScores);
    }

    private final Statement selectionBlock;
    
    private AttributiveSelection(HeuristicSelectionProcesses hsp,
                                 DomainBarrier db,
                                 AttributiveSelectionHandler ash,
                                 LLHeuristicsHandler llhh,
                                 TrackableSolutionHandler tsh,
                                 double amplFactor)
    {
        RouletteSelector<HeuristicSelectionScore> selector = new RouletteSelector<>(
            llhh.getHeuristicsCount(),
            (hss) -> hss.getScore()
        );
        selector.setAmplificator(amplFactor);
        
        Comparator<? super HeuristicSelectionScore> comp = (x, y) -> {
            if(x.getScore() > y.getScore())
                return -1;
            else if(x.getScore() < y.getScore())
                return 1;
            else
                return 0;
        };
        
        selectionBlock = block(
            hsp::setSolutionToProcessAsInput,
            If(OR(tsh::outputImprovedBest, ash::shouldChangeHeuristic)).then(
                selector::clear,
                ash.configSelectorByChangeImpact(selector),
                () -> selector.sortElements(comp),
                ash.selectHeuristicAndRemove(selector)
            ),
            db.callHeuristic(),
            ash::updateStatsOfCurrentHeuristic,
            hsp::storeOutputAsProcessed
        );
        
        /*
        selectionBlock = block(
            hsp::setSolutionToProcessAsInput,
            selector::clear,
            ash.configSelectorByChangeImpact(selector),
            () -> selector.sortElements(comp),
            repeat(
                ash.selectHeuristicAndRemove(selector),
                db.callHeuristic(),
                ash::updateStatsOfCurrentHeuristic
            ).until(OR(NOT(hsp::solutionNotChanged), selector::isEmpty)),
            hsp::storeOutputAsProcessed
        );
        */
        
        /*
        selectionBlock = block(
            hsp::setSolutionToProcessAsInput,
            If(ash::mustChangeHeuristic).then(
                ash::changeToRandomHeuristic
            ),
            db.callHeuristic(),
            ash::updateStatsOfCurrentHeuristic,
            ash::evaluateHeuristicChange,
            hsp::storeOutputAsProcessed
        );
        */
    }
}

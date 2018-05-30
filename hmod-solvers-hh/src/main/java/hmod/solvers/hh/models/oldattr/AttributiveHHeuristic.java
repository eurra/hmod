package hmod.solvers.hh.models.oldattr;

import static hmod.core.FlowchartFactory.*;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.hh.models.basicops.GenericSelectionHHeuristic;
import hmod.solvers.hh.models.basicops.HeuristicSelectionProcesses;
import hmod.solvers.hh.models.selection.DomainBarrier;
import hmod.solvers.hh.models.selection.HeuristicRunnerHandler;
import hmod.solvers.hh.models.selection.LLHeuristicsHandler;
import hmod.solvers.hh.models.soltrack.SolutionTrackingHandler;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;

public final class AttributiveHHeuristic {
    public static final Parameter<Double> OSCILLATION_MODIFIER = new Parameter("AttributiveHHeuristic.OSCILLATION_MODIFIER");
    public static final Parameter<Double> GROW_PROPORTION = new Parameter("AttributiveHHeuristic.GROW_PROPORTION");
    public static final Parameter<Integer> AMPLIFICATION_FACTOR = new Parameter("AttributiveHHeuristic.AMPLIFICATION_FACTOR");

    @LoadsComponent(value={StrategicOscillationHandler.class, StrategicOscillationProcesses.class})
    public static void load(ComponentRegister cr, 
                            ParameterRegister pr, 
                            DomainBarrier db, 
                            LLHeuristicsHandler llhh,
                            IterationHandler ih, 
                            HeuristicRunnerHandler hrh,
                            TrackableSolutionHandler sh, 
                            HeuristicSelectionProcesses hsp, 
                            SolutionTrackingHandler sth, 
                            GenericSelectionHHeuristic gshh, 
                            IterativeHeuristic iHeu)
    {
        StrategicOscillationHandler soh = cr.provide(new StrategicOscillationHandler(
            pr.getRequiredValue(OSCILLATION_MODIFIER), 
            pr.getRequiredValue(GROW_PROPORTION), 
            pr.getRequiredValue(AMPLIFICATION_FACTOR)
        ));
        
        HeuristicScoringHandler hsh = new HeuristicScoringHandler(llhh);
        StrategicOscillationProcesses sop = cr.provide(new StrategicOscillationProcesses(ih, soh, hrh, hsh, sh, llhh, sth));
        
        db.callHeuristic().appendAfter(block(
            sop::updateStatsOfCurrentHeuristic,
            If(AND(NOT(soh::isOscillationEnabled), sh::outputImprovedInput)).then(
                soh::enableOscillation,
                soh::updateOscillationRate
            ).ElseIf(soh::isOscillationEnabled).then(
                If(sh::outputImprovedBest).then(
                    soh::restartPiMultiplier,
                    soh::updateOscillationRate
                ).Else(
                    sop::updatePIMultiplier,
                    If(NOT(soh::checkPIMultiplier)).then(
                        soh::disableOscillation
                    ).Else(
                        soh::updateOscillationRate
                    )
                )
            )
        ));
        
        gshh.heuristicSelection().apply(base -> block(
            If(NOT(soh::isOscillationEnabled)).then(
                base
            ).Else(
                hsp::setSolutionToProcessAsInput,
                db.callHeuristic(),
                sop::selectHeuristicByChangeImpact,
                hsp::storeOutputAsProcessed
            )
        ));
        
        iHeu.finish().append(sop::printHeuristicScores);
    }
}
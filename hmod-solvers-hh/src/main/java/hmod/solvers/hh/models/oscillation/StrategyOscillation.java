
package hmod.solvers.hh.models.oscillation;

import static hmod.core.FlowchartFactory.*;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.hh.models.selection.DomainBarrier;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import hmod.solvers.hh.models.tabulist.TabuList;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class StrategyOscillation
{
    public static final Parameter<Double> OSCILLATION_MODIFIER = new Parameter<>("StrategyOscillation.OSCILLATION_MODIFIER");
    public static final Parameter<Double> GROW_PROPORTION = new Parameter<>("StrategyOscillation.GROW_PROPORTION");
    
    @LoadsComponent(OscillationHandler.class)
    public static void load(ComponentRegister cr,
                            ParameterRegister pr,
                            TabuList tl,
                            TrackableSolutionHandler sh,
                            IterativeHeuristic ih,
                            DomainBarrier db)
    {
        
        double oscillationModifier = pr.getRequiredValue(OSCILLATION_MODIFIER);
        double growProportion = pr.getRequiredValue(GROW_PROPORTION);
        OscillationHandler oh = cr.provide(new OscillationHandler(tl, sh, oscillationModifier, growProportion));
        
        /*
        gshh.heuristicSelection().prependBefore(block(
            If(oh::isOscillationEnabled).then(
                oh::updatePIMultiplier,
                oh::updateOscillationRate
            )
        ));
        */
        
        ih.initReporting().append(block(
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "'Strategy Oscillation' enabled with:"),
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "- Oscillation modifier: " + oscillationModifier),
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "- Grow proportion: " + growProportion)
        ));
        
        db.callHeuristic().prependAfter(block(
            If(oh::isOscillationEnabled).then(
                oh::updatePIMultiplier,
                oh::updateOscillationRate
            )
        ));
        
        /*
        db.callHeuristic().prependAfter(block(
            If(oh::isOscillationEnabled).then(
                If(sh::outputImprovedBest).then(
                    oh::restartPiMultiplier
                ).ElseIf(oh::checkPIMultiplier).then(
                    oh::updatePIMultiplier
                ),
                oh::updateOscillationRate
            )
        ));
        */
    }

    private StrategyOscillation()
    {
    }
}

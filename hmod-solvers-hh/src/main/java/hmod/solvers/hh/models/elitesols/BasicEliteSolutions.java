package hmod.solvers.hh.models.elitesols;

import static hmod.core.FlowchartFactory.*;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.hh.HHSolutionHandler;
import hmod.solvers.hh.models.basicops.BasicOperators;
import hmod.solvers.hh.models.basicops.GenericSelectionHHeuristicProcesses;
import hmod.solvers.hh.models.basicops.HeuristicSelectionProcesses;
import hmod.solvers.hh.models.selection.DomainBarrier;
import hmod.solvers.hh.models.soltrack.SolutionTrackingProcesses;
import java.util.function.Function;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.util.output.OutputManager;

public final class BasicEliteSolutions {
    public static final Parameter<Integer> MAX_ELITES = new Parameter("BasicEliteSolutions.MAX_ELITES");
    public static final Parameter<Integer> REPLACE_ITERATIONS = new Parameter("BasicEliteSolutions.REPLACE_ITERATIONS");
    public static final Parameter<EliteReplaceCriteria> REPLACE_CRITERIA = new Parameter("BasicEliteSolutions.REPLACE_CRITERIA");
    public static final Parameter<Double> ROULETTE_CRITERIA_AMPLIFICATOR = new Parameter("BasicEliteSolutions.ROULETTE_CRITERIA_AMPLIFICATOR");

    @LoadsComponent(value={EliteSolutionsHandler.class, EliteSolutionsProcesses.class})
    public static void loadComponent(ComponentRegister cr, ParameterRegister pr, HHSolutionHandler sh, IterationHandler ih, IterativeHeuristic iHeu) {
        int maxElites = pr.getRequiredValue(MAX_ELITES);
        EliteSolutionsHandler esh = (EliteSolutionsHandler)cr.provide(new EliteSolutionsHandler(maxElites));
        cr.provide(new EliteSolutionsProcesses(esh, sh, ih));
        iHeu.initReporting().append(() -> {
            OutputManager.println((String)"hmod.solvers.common.executionInfo", (Object)("Max. elite solutions: " + maxElites));
        }
        );
    }

    @LoadsComponent
    public static void loadDomainBarrier(DomainBarrier db, EliteSolutionsProcesses esp) {
        db.callHeuristic().prependAfter(esp::updateEliteSolutions);
    }

    @LoadsComponent
    public static void loadGlobalReplacing(ParameterRegister pr,
                                           EliteSolutionsProcesses esp,
                                           SolutionTrackingProcesses stp, 
                                           IterativeHeuristic iHeu, 
                                           GenericSelectionHHeuristicProcesses gshhp,
                                           HeuristicSelectionProcesses hsp,
                                           BasicOperators ba)
    {
        Function selector;
        int updateIterations = pr.getRequiredValue(REPLACE_ITERATIONS);
        EliteReplaceCriteria criteria = (EliteReplaceCriteria)((Object)pr.getRequiredValue(REPLACE_CRITERIA));
        switch (criteria) {
            case ROULETTE: {
                Double ampl = pr.getValue(ROULETTE_CRITERIA_AMPLIFICATOR);
                if (ampl == null) {
                    ampl = 1.0;
                }
                selector = EliteSolutionsProcesses.rouletteEliteSelector(ampl);
                break;
            }
            default: {
                selector = EliteSolutionsProcesses.randomEliteSelector();
            }
        }
        
        iHeu.initReporting().append(block(
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Update iterations for elite solutions: " + updateIterations),
            () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Replacement criteria: " + criteria)
        ));
        
        if (criteria == EliteReplaceCriteria.ROULETTE && pr.getValue(ROULETTE_CRITERIA_AMPLIFICATOR) != null) {
            iHeu.initReporting().append(() -> {
                OutputManager.println((String)"hmod.solvers.common.executionInfo", (Object)("Amplification for " + (Object)((Object)criteria) + " criteria: " + pr.getValue(ROULETTE_CRITERIA_AMPLIFICATOR)));
            }
            );
        }
        
        iHeu.iteration().appendAfter(
            If(AND(
                stp.areIterationsPassedSinceGlobalImprove(updateIterations), 
                esp.areIterationsPassedSinceLastReplace(updateIterations)
            )).then(
                esp.replaceOutputSolutionWithElite(selector),
                hsp::storeOutputAsProcessed,
                gshhp::storeProcessedSolutionAsCandidate,
                ba.maMethod(BasicOperators.ACCEPT_ALL_MOVES)
            )
        );
    }

}

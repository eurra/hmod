
package hmod.solvers.hh;

import hmod.domains.darptw.DARPTWDomain;
import hmod.domains.darptw.DARPTWOutputIds;
import hmod.solvers.common.Heuristic;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.hh.adapters.darptw.DARPTWAdapter;
import hmod.solvers.hh.adapters.darptw.DARPTWAttributiveAdapter;
import hmod.solvers.hh.models.adapter.AdapterBarrier;
import hmod.solvers.hh.models.attr.AttributiveSelection;
import hmod.solvers.hh.models.basicops.BasicOperators;
import hmod.solvers.hh.models.basicops.GenericSelectionHHeuristic;
import hmod.solvers.hh.models.attr.AttributiveAcceptance;
import hmod.solvers.hh.models.attr.ReplaceCriteria;
import hmod.solvers.hh.models.oscillation.StrategyOscillation;
import hmod.solvers.hh.models.selection.SelectionHHeuristic;
import hmod.solvers.hh.models.selection.SelectionHHeuristicOutputIds;
import hmod.solvers.hh.models.soltrack.DefaultSolutionTracking;
import hmod.solvers.hh.models.tabulist.SimpleTabuList;
import java.io.IOException;
import optefx.loader.ModuleLoader;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Enrique Urra C.
 */
public class DARPTWTest
{    
    @BeforeClass
    public static void init()
    {
        OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
            addSystemOutputId(HeuristicOutputIds.EXECUTION_INFO).
            //addSystemOutputId(AttributiveSelectionOutputIds.INFO).
            addSystemOutputId(SelectionHHeuristicOutputIds.NEW_BEST_FITNESS).
            addSystemOutputId(DARPTWOutputIds.RESULT_DETAIL).
            addSystemOutputId(DARPTWOutputIds.OPERATION_INFO)//.
            //addSystemOutputId(EliteSolutionsOutputIds.EVENTS)
        ); 
    }
    
    private ModuleLoader loadCore()
    {
        return new ModuleLoader().            
            loadAll(IterativeHeuristic.class, SelectionHHeuristic.class,
                GenericSelectionHHeuristic.class, BasicOperators.class, 
                DefaultSolutionTracking.class, AdapterBarrier.class, 
                DARPTWAdapter.class, DARPTWDomain.class
            );
    }
    
    private ModuleLoader loadEliteModel(ModuleLoader loader)
    {
        return loader.loadAll(
            AttributiveAcceptance.class, 
            StrategyOscillation.class,
            SimpleTabuList.class
        );
    }
    
    private ModuleLoader loadAttributiveModel(ModuleLoader loader)
    {
        return loader.loadAll(
                AttributiveSelection.class, 
                DARPTWAttributiveAdapter.class,
                StrategyOscillation.class,
                SimpleTabuList.class
        );
    }
    
    private void configureDARPTWParams(ModuleLoader loader)
    {
        loader.
            setParameter(DARPTWDomain.WEIGHT_TRANSIT_TIME, 8.0).
            setParameter(DARPTWDomain.WEIGHT_RIDE_TIME, 0.0).
            setParameter(DARPTWDomain.WEIGHT_EXCESS_RIDE_TIME, 3.0).
            setParameter(DARPTWDomain.WEIGHT_WAIT_TIME, 1.0).
            setParameter(DARPTWDomain.WEIGHT_SLACK_TIME, 0.0).
            setParameter(DARPTWDomain.WEIGHT_ROUTE_DURATION, 1.0).
            setParameter(DARPTWDomain.WEIGHT_TIME_WINDOWS_VIOLATION, 24.0).
            setParameter(DARPTWDomain.WEIGHT_MAXIMUM_RIDE_TIME_VIOLATION, 24.0).
            setParameter(DARPTWDomain.WEIGHT_MAXIMUM_ROUTE_DURATION_VIOLATION, 24.0);
    }
    
    @Test
    public void defaultModel() throws IOException
    {
        ModuleLoader loader = loadCore().
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, BasicOperators.GREEDY).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, BasicOperators.IMPROVING_OR_EQUALS).
            setParameter(DARPTWDomain.INSTANCE, "../launcher-testing/input/problems/darptw/pr01.txt");
        
        configureDARPTWParams(loader);        
        loader.getInstance(Heuristic.class).run();
    }
    
    @Test
    public void eliteModel() throws IOException
    {
        ModuleLoader loader = loadCore();
        loadEliteModel(loader);
        
        loader.
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(AttributiveAcceptance.MAX_SOLUTIONS, 100).
            //setParameter(AttributiveAcceptance.REPLACE_ITERATIONS, 20).
            setParameter(AttributiveAcceptance.REPLACE_CRITERIA, ReplaceCriteria.ROULETTE).
            setParameter(AttributiveAcceptance.ROULETTE_CRITERIA_AMPLIFICATOR, 10.0).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, BasicOperators.GREEDY).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, BasicOperators.IMPROVING_OR_EQUALS).
            setParameter(StrategyOscillation.OSCILLATION_MODIFIER, 0.5).
            setParameter(StrategyOscillation.GROW_PROPORTION, 0.5).
            setParameter(AttributiveAcceptance.QUALITY_TOLERANCE, 0.5).
            setParameter(DARPTWDomain.INSTANCE, "../launcher-testing/input/problems/darptw/pr19.txt");
        
        configureDARPTWParams(loader);
        loader.getInstance(Heuristic.class).run();
    }
    
    @Test
    public void attrModel() throws IOException
    {
        ModuleLoader loader = loadCore();
        loadAttributiveModel(loader);
        
        loader.
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, BasicOperators.GREEDY).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, BasicOperators.IMPROVING_OR_EQUALS).
            setParameter(DARPTWDomain.INSTANCE, "../launcher-testing/input/problems/darptw/pr01.txt").
            setParameter(StrategyOscillation.OSCILLATION_MODIFIER, 0.5).
            setParameter(StrategyOscillation.GROW_PROPORTION, 0.9).
            setParameter(AttributiveSelection.AMPLIFICATION_FACTOR, 100.0);
        
        configureDARPTWParams(loader);
        loader.getInstance(Heuristic.class).run();
    }
    
    @Test
    public void attrAndEliteModel() throws IOException
    {
        ModuleLoader loader = loadCore();
        loadAttributiveModel(loader);
        loadEliteModel(loader);
        
        loader.
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(AttributiveAcceptance.MAX_SOLUTIONS, 100).
            //setParameter(AttributiveAcceptance.REPLACE_ITERATIONS, 5).
            setParameter(AttributiveAcceptance.REPLACE_CRITERIA, ReplaceCriteria.RANDOM).
            //setParameter(AttributiveAcceptance.ROULETTE_CRITERIA_AMPLIFICATOR, 10.0).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, BasicOperators.GREEDY).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, BasicOperators.IMPROVING_OR_EQUALS).
            setParameter(DARPTWDomain.INSTANCE, "../launcher-testing/input/problems/darptw/pr01.txt").
            setParameter(StrategyOscillation.OSCILLATION_MODIFIER, 0.5).
            setParameter(StrategyOscillation.GROW_PROPORTION, 0.9).
            setParameter(AttributiveAcceptance.QUALITY_TOLERANCE, 0.5).    
            setParameter(AttributiveSelection.AMPLIFICATION_FACTOR, 100.0);
        
        configureDARPTWParams(loader);
        loader.getInstance(Heuristic.class).run();
    }
}

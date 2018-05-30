
package hmod.solvers.hh;

import hmod.domains.mkp.MKPDomain;
import hmod.domains.mkp.MKPOutputIds;
import hmod.solvers.common.Heuristic;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.hh.adapters.mkp.MKPDomainAdapter;
import hmod.solvers.hh.adapters.mkp.MKPDomainAttributiveAdapter;
import hmod.solvers.hh.models.adapter.AdapterBarrier;
import hmod.solvers.hh.models.attr.AttributiveSelection;
import hmod.solvers.hh.models.basicops.BasicOperators;
import hmod.solvers.hh.models.basicops.GenericSelectionHHeuristic;
import hmod.solvers.hh.models.attr.AttributiveAcceptance;
import hmod.solvers.hh.models.attr.FrequencyAcceptance;
import hmod.solvers.hh.models.attr.ReplaceCriteria;
import hmod.solvers.hh.models.oscillation.StrategyOscillation;
import hmod.solvers.hh.models.selection.SelectionHHeuristic;
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
public class MKPTest
{
    public MKPTest()
    {
    }
    
    @BeforeClass
    public static void init()
    {
        OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
            addSystemOutputId(HeuristicOutputIds.EXECUTION_INFO).
            addSystemOutputId(MKPOutputIds.FINAL_SOLUTION_INFO)
        ); 
    }
    
    private ModuleLoader loadCore()
    {
        return new ModuleLoader().            
            loadAll(IterativeHeuristic.class, SelectionHHeuristic.class,
                GenericSelectionHHeuristic.class, BasicOperators.class, 
                DefaultSolutionTracking.class, AdapterBarrier.class, 
                MKPDomainAdapter.class, MKPDomain.class
            );
    }
    
    private ModuleLoader loadAttributiveSelection(ModuleLoader loader)
    {
        return loader.loadAll(
            SimpleTabuList.class,
            StrategyOscillation.class, 
            AttributiveSelection.class, 
            MKPDomainAttributiveAdapter.class
        );
    }
    
    private ModuleLoader loadAttributiveAcceptance(ModuleLoader loader)
    {
        return loader.loadAll(
            SimpleTabuList.class,
            StrategyOscillation.class, 
            AttributiveAcceptance.class, 
            MKPDomainAttributiveAdapter.class
        );
    }
    
    private ModuleLoader loadFrequencyAcceptance(ModuleLoader loader)
    {
        return loader.loadAll(
            SimpleTabuList.class,
            StrategyOscillation.class, 
            FrequencyAcceptance.class, 
            MKPDomainAttributiveAdapter.class
        );
    }
    
    @Test
    public void defaultModel() throws IOException
    {
        ModuleLoader loader = loadCore().
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, BasicOperators.GREEDY).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, BasicOperators.IMPROVING_OR_EQUALS).
            setParameter(MKPDomain.FILL_METHOD, MKPDomain.RANDOM_FILL).
            setParameter(MKPDomain.INSTANCE, "../launcher-testing/input/problems/mkp/mknapcb1.txt:0").
            setParameter(MKPDomain.LP_OPTIMUM_SET, "../launcher-testing/input/problems/mkp/mknapcb-lp-opt.txt");
        
        loader.getInstance(Heuristic.class).run();
    }
    
    @Test
    public void attributiveSelection() throws IOException
    {
        ModuleLoader loader = loadCore().
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, BasicOperators.IMPROVING_OR_EQUALS).
            setParameter(MKPDomain.FILL_METHOD, MKPDomain.RANDOM_FILL).
            setParameter(MKPDomain.INSTANCE, "../launcher-testing/input/problems/mkp/mknapcb1.txt:0").
            setParameter(MKPDomain.LP_OPTIMUM_SET, "../launcher-testing/input/problems/mkp/mknapcb-lp-opt.txt");
                
        loadAttributiveSelection(loader).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, AttributiveSelection.ATTRIBUTIVE_SELECTION).
            setParameter(SimpleTabuList.TABU_LIST_SIZE, 100).
            setParameter(StrategyOscillation.OSCILLATION_MODIFIER, 0.5).
            setParameter(StrategyOscillation.GROW_PROPORTION, 0.05).
            setParameter(AttributiveSelection.AMPLIFICATION_FACTOR, 1.0);
        
        loader.getInstance(Heuristic.class).run();
    }
    
    @Test
    public void attributiveAcceptance() throws IOException
    {
        ModuleLoader loader = loadCore().
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, BasicOperators.GREEDY).
            setParameter(MKPDomain.FILL_METHOD, MKPDomain.RANDOM_FILL).
            setParameter(MKPDomain.INSTANCE, "../launcher-testing/input/problems/mkp/mknapcb1.txt:0").
            setParameter(MKPDomain.LP_OPTIMUM_SET, "../launcher-testing/input/problems/mkp/mknapcb-lp-opt.txt");
        
        loadAttributiveAcceptance(loader).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, AttributiveAcceptance.ATTRIBUTIVE_ACCEPTANCE).
            setParameter(SimpleTabuList.TABU_LIST_SIZE, 100).
            setParameter(StrategyOscillation.OSCILLATION_MODIFIER, 0.5).
            setParameter(StrategyOscillation.GROW_PROPORTION, 0.05).
            setParameter(AttributiveAcceptance.MAX_SOLUTIONS, 50).
            setParameter(AttributiveAcceptance.QUALITY_TOLERANCE, 0.5).
            setParameter(AttributiveAcceptance.REPLACE_CRITERIA, ReplaceCriteria.ROULETTE).
            setParameter(AttributiveAcceptance.ROULETTE_CRITERIA_AMPLIFICATOR, 1.0);
        
        loader.getInstance(Heuristic.class).run();
    }
    
    @Test
    public void frequencyAcceptance()
    {
        ModuleLoader loader = loadCore().
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, BasicOperators.GREEDY).
            setParameter(MKPDomain.FILL_METHOD, MKPDomain.RANDOM_FILL).
            setParameter(MKPDomain.INSTANCE, "../launcher-testing/input/problems/mkp/mknapcb1.txt:0").
            setParameter(MKPDomain.LP_OPTIMUM_SET, "../launcher-testing/input/problems/mkp/mknapcb-lp-opt.txt");
        
        loadFrequencyAcceptance(loader).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, FrequencyAcceptance.FREQUENCY_ACCEPTANCE).
            setParameter(SimpleTabuList.TABU_LIST_SIZE, 10).
            setParameter(StrategyOscillation.OSCILLATION_MODIFIER, 0.5).
            setParameter(StrategyOscillation.GROW_PROPORTION, 0.05).
            setParameter(FrequencyAcceptance.QUALITY_POOL_SIZE, 100).
            setParameter(FrequencyAcceptance.QUALITY_TOLERANCE, 0.25).
            setParameter(FrequencyAcceptance.CONVERGENCE_SPEED_TOLERANCE, 0.25);
        
        loader.getInstance(Heuristic.class).run();
    }
    
    @Test
    public void attributiveComplete() throws IOException
    {
        ModuleLoader loader = loadCore().
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(MKPDomain.FILL_METHOD, MKPDomain.RANDOM_FILL).
            setParameter(MKPDomain.INSTANCE, "../launcher-testing/input/problems/mkp/mknapcb1.txt:0").
            setParameter(MKPDomain.LP_OPTIMUM_SET, "../launcher-testing/input/problems/mkp/mknapcb-lp-opt.txt");
        
        
        
        loadAttributiveSelection(loader).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, AttributiveSelection.ATTRIBUTIVE_SELECTION).
            setParameter(AttributiveSelection.AMPLIFICATION_FACTOR, 1.0);
        
        loadAttributiveAcceptance(loader).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, AttributiveAcceptance.ATTRIBUTIVE_ACCEPTANCE).
            setParameter(AttributiveAcceptance.MAX_SOLUTIONS, 50).
            setParameter(AttributiveAcceptance.QUALITY_TOLERANCE, 0.5).
            setParameter(AttributiveAcceptance.REPLACE_CRITERIA, ReplaceCriteria.ROULETTE).
            setParameter(AttributiveAcceptance.ROULETTE_CRITERIA_AMPLIFICATOR, 1.0);
        
        loader.setParameter(SimpleTabuList.TABU_LIST_SIZE, 100).
            setParameter(StrategyOscillation.OSCILLATION_MODIFIER, 0.5).
            setParameter(StrategyOscillation.GROW_PROPORTION, 0.05);
        
        loader.getInstance(Heuristic.class).run();
    }
}

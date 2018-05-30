
package hmod.solvers.hh;

import hmod.domains.binary.BinaryDomain;
import hmod.solvers.common.Heuristic;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.hh.adapters.binary.BinaryDomainAdapter;
import hmod.solvers.hh.models.adapter.AdapterBarrier;
import hmod.solvers.hh.models.basicops.BasicOperators;
import hmod.solvers.hh.models.basicops.GenericSelectionHHeuristic;
import hmod.solvers.hh.models.selection.SelectionHHeuristic;
import hmod.solvers.hh.models.selection.SelectionHHeuristicOutputIds;
import hmod.solvers.hh.models.soltrack.DefaultSolutionTracking;
import optefx.loader.ModuleLoader;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Enrique Urra C.
 */
public class BinaryTest
{     
    public BinaryTest()
    {
    }
    
    @BeforeClass
    public static void init()
    {
        OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
            addSystemOutputId(HeuristicOutputIds.EXECUTION_INFO).
            addSystemOutputId(SelectionHHeuristicOutputIds.NEW_BEST_FITNESS).
            addSystemOutputId("binExample-result-info")//.
            //addSystemOutputId(EliteSolutionsOutputIds.EVENTS)
        ); 
    }
    
    private ModuleLoader loadCore()
    {
        return new ModuleLoader().            
            loadAll(IterativeHeuristic.class, SelectionHHeuristic.class,
                GenericSelectionHHeuristic.class, BasicOperators.class, 
                DefaultSolutionTracking.class, AdapterBarrier.class, 
                BinaryDomainAdapter.class, BinaryDomain.class
            );
    }
    
    @Test
    public void defaultModel()
    {
        ModuleLoader loader = loadCore().
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 500).
            setParameter(GenericSelectionHHeuristic.HEURISTIC_SELECTION, BasicOperators.GREEDY).
            setParameter(GenericSelectionHHeuristic.MOVE_ACCEPTANCE, BasicOperators.IMPROVING_OR_EQUALS);
        
        loader.getInstance(Heuristic.class).run();
    }
}

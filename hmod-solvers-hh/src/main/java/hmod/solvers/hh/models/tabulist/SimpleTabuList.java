
package hmod.solvers.hh.models.tabulist;

import hmod.core.Statement;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.hh.HHSolution;
import hmod.solvers.hh.models.basicops.GenericSelectionHHeuristic;
import hmod.solvers.hh.models.basicops.MoveAcceptanceHandler;
import hmod.solvers.hh.models.selection.DomainBarrier;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class SimpleTabuList
{
    private static Statement queueInTabu(MoveAcceptanceHandler mah, TabuList list)
    {
        return () -> {
            HHSolution sol = mah.retrieveCurrentlyAccepted();
            list.tryQueue(sol);
        };
    }
    
    public static final Parameter<Integer> TABU_LIST_SIZE = new Parameter<>("SimpleTabuList.TABU_LIST_SIZE");
    
    @LoadsComponent(TabuList.class)
    public static void load(ComponentRegister cr,
                            ParameterRegister pr,
                            IterativeHeuristic ih,
                            MoveAcceptanceHandler mah,
                            GenericSelectionHHeuristic gshh,
                            DomainBarrier db)
    {
        Integer size = pr.isValueSet(TABU_LIST_SIZE) ? pr.getValue(TABU_LIST_SIZE) : 100;
        TabuList tb = cr.provide(new TabuList<>(size));
        
        ih.initReporting().append(() -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Using 'Simple Tabu List' with size " + size));
        gshh.moveAcceptance().appendAfter(queueInTabu(mah, tb));
    }
}

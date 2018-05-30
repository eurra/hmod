
package hmod.solvers.hh.models.selection;

import static hmod.core.FlowchartFactory.block;
import hmod.solvers.common.IterativeHeuristic;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;

/**
 *
 * @author Enrique Urra C.
 */
public final class SelectionHHeuristic
{    
    @LoadsComponent({
        LLHeuristicsHandler.class,
        LLHeuristicsRegister.class,
        SelectionHHeuristicProcesses.class
    })
    public static void loadData(ComponentRegister cr,
                                HeuristicRunnerHandler hrh)
    {
        MutableHeuristicsHandler llhh = cr.provide(new MutableHeuristicsHandler(), LLHeuristicsHandler.class, LLHeuristicsRegister.class);
        cr.provide(new SelectionHHeuristicProcesses(llhh, hrh));
    }
    
    @LoadsComponent({
        DomainBarrier.class,
        HeuristicRunnerHandler.class
    })
    public static void loadBarrier(ComponentRegister cr)
    {
        HeuristicRunnerHandler hrh = cr.provide(new HeuristicRunnerHandler());
        cr.provide(new DomainBarrier(hrh));
    }
    
    @LoadsComponent({ 
        IterativeHeuristic.class
    })
    public static void loadHeuristic(ComponentRegister cr,
                                     IterativeHeuristic iHeu,
                                     DomainBarrier db,
                                     SelectionHHeuristicProcesses shhProc)
    {
        iHeu.init().append(block(
            shhProc::selectInitHeuristic,
            db.callHeuristic()
        ));
        
        iHeu.initReporting().append(shhProc::printLowLevelHeuristics);
        
        iHeu.finish().append(block(
            shhProc::selectFinishHeuristic,
            db.callHeuristic()
        ));
    }

    private SelectionHHeuristic()
    {
    }
}

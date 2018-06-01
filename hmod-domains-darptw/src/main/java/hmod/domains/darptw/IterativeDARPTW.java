
package hmod.domains.darptw;

import static hmod.core.FlowchartFactory.block;
import hmod.core.Routine;
import hmod.core.Statement;
import hmod.core.ValidableRoutine;
import hmod.solvers.common.HeuristicOutputIds;
import hmod.solvers.common.IterationHandler;
import hmod.solvers.common.IterativeHeuristic;
import hmod.solvers.common.TimeElapsedHandler;
import java.util.function.Supplier;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Processable;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class IterativeDARPTW
{
    private static Statement print(Supplier<String> sup)
    {
        return () -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, sup.get());
    }
    
    @LoadsComponent(IterativeDARPTW.class)
    public static void load(ComponentRegister cr,
                            IterativeHeuristic iHeu,
                            IterationHandler ih,
                            TimeElapsedHandler teh,
                            DARPTWDomain darptw)
    {
        cr.provide(new IterativeDARPTW(iHeu, ih, teh, darptw));
    }
    
    private final ValidableRoutine main = new ValidableRoutine("IterativeDARPTW.main");

    private IterativeDARPTW(IterativeHeuristic iHeu,
                            IterationHandler ih,
                            TimeElapsedHandler teh,
                            DARPTWDomain darptw)
    {
        main.append(block(
            print(() -> "This is the iteration number " + ih.getCurrentIteration()),
            print(() -> "There are " + teh.getElapsedSeconds() + " secs. elapsed"),
            darptw.heuristic(DARPTWDomain.MOVE_RANDOM_CLIENT)
        ));
        
        iHeu.initReporting().append(
            print(() -> "This is printed at the start.")
        );
        
        iHeu.init().append(darptw.loadNewSolution());        
        iHeu.iteration().append(main);        
        iHeu.finish().append(darptw.saveSolution());
        
        iHeu.finishReporting().append(block(
            print(() -> "This is printed at the end."),
            darptw.reportSolution()
        ));
    }

    public Routine main() { return main; }
    @Processable private void process() { main.validate(); }
}

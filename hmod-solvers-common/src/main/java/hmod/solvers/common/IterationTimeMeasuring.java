
package hmod.solvers.common;

import static hmod.core.AlgorithmFactory.*;
import static hmod.core.MethodBridges.*;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class IterationTimeMeasuring
{
    /*
    public static void load(IterativeHeuristic ih)
    {
        IterationVar iv = ih.main.getScope().getVar(IterationVar.class);
        TimeMeasureVar data = new TimeMeasureVar();
        
        ih.initBlock.appendAfter(data.init);
    
        ih.iterationBlock.appendAfter(block(
            data.addReading,
            If(data.checkMessageShow).then(
                data.showMessage
            )
        ));
    
        ih.initReportingBlock.append(
            run(() -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Showing iterations delay every " + delay + " iterations"))
        );
    }
    */
    
    public static final Parameter<Integer> ITERATIONS_DELAY = new Parameter<>("IterationTimeMeasuring.ITERATIONS_DELAY");
    
    @LoadsComponent
    public static void load(ParameterRegister pr,
                            IterativeHeuristic iHeu)
    {
        int delay = pr.isValueSet(ITERATIONS_DELAY) ? pr.getValue(ITERATIONS_DELAY) : 50;
        TimeMeasureVar data = new TimeMeasureVar();
        
        iHeu.initBlock().appendAfter(block("IterationTimeMeasuring.init",
            data.setNew(iHeu.iteration(), refOf(delay)),
            data.init()
        ));
        
        iHeu.initReportingBlock().append(block("IterationTimeMeasuring.initReporting",
            run(() -> OutputManager.println(HeuristicOutputIds.EXECUTION_INFO, "Showing iterations delay every " + delay + " iterations"))
        ));
        
        iHeu.iterationBlock().appendAfter(block("IterationTimeMeasuring.iteration",
            data.addReading(),
            If(data.checkMessageShow()).then(
                data.showMessage()
            )
        ));
    }
}

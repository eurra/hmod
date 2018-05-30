
package hmod.solvers.common;

import static hmod.core.AlgorithmFactory.*;
import hmod.core.Context;
import hmod.core.Statement;
import hmod.core.Routine;
import hmod.core.Procedure;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;

/**
 *
 * @author Enrique Urra C.
 */
public class IterativeHeuristic implements Heuristic
{    
    public static final Parameter<Integer> MAX_ITERATIONS = new Parameter<>("IterativeHeuristic.MAX_ITERATIONS");
    public static final Parameter<Double> MAX_SECONDS = new Parameter<>("IterativeHeuristic.MAX_SECONDS");
        
    @LoadsComponent(IterativeHeuristic.class)
    public static void load(ComponentRegister cr, ParameterRegister pr)
    {
        cr.provide(new IterativeHeuristic(pr));
    }
    
    /*
    @LoadsComponent(IterativeHeuristic.class)
    public static void load(ComponentRegister cr, ParameterRegister pr)
    {
        cr.provide(new IterativeHeuristic(pr));
    }
    */
     
    private final Statement main;
    private final Routine initBlock = new Routine("IterativeHeuristic.init");
    private final Routine initReportingBlock = new Routine("IterativeHeuristic.initReporting", false);
    private final Routine iterationBlock = new Routine("IterativeHeuristic.iteration");
    private final Routine finishBlock = new Routine("IterativeHeuristic.finish");
    private final Routine finishReportingBlock = new Routine("IterativeHeuristic.finishReporting", false);
    private final IterationVar iteration = new IterationVar("IterativeHeuristic.iteration");
    private final TimeElapsedVar timeElapsed = new TimeElapsedVar("IterativeHeuristic.timeElapsed");
    private final FinishControlVar finishControl = new FinishControlVar("IterativeHeuristic.finishControl");

    /*    
    public IterativeHeuristic(ParameterRegister pr)
    {
        this(new AlgorithmScope(), pr);
    }
    
    public IterativeHeuristic(AlgorithmScope as, ParameterRegister pr)
    {
        int maxIterations = pr.getRequiredValue(MAX_ITERATIONS);
        double maxSeconds = pr.isValueSet(MAX_SECONDS) ? pr.getValue(MAX_SECONDS) : -1.0;
    
        ForwardIterationVar iv = as.setVar(new ForwardIterationVar(maxIterations), IterationVar.class);
        SwitchTimeElapsedVar tev = as.setVar(new SwitchTimeElapsedVar(maxSeconds), TimeElapsedVar.class);
        FinishControlVar fc = as.setVar(new FinishControlVar());
        ReportingOps repOps = ReportingOps.getInstance();
    
        main = block(as,
            tev.startTime,
            initBlock,
            repOps.printMaxIterations,
            repOps.printMaxSeconds,
            repOps.printRandomSeed,
            initReportingBlock,
            While(NOT(OR(iv.isFinished, tev.isFinished, fc.isFinished))).Do(
                iterationBlock,
                iv.advance
            ),
            finishBlock,
            tev.endTime,
            repOps.printTotalSeconds,
            finishReportingBlock
        );
    }
    */
    
    private IterativeHeuristic(ParameterRegister pr)
    {
        int maxIterations = pr.getRequiredValue(MAX_ITERATIONS);
        double maxSeconds = pr.isValueSet(MAX_SECONDS) ? pr.getValue(MAX_SECONDS) : -1.0;
        
        ReportingOps repOps = ReportingOps.getInstance();
        ForwardIterationVar iv = new ForwardIterationVar("IterativeHeuristic.iteration");
        SwitchTimeElapsedVar tev = new SwitchTimeElapsedVar("IterativeHeuristic.timeElapsed");
        
        main = block("IterativeHeuristic.main",
            iteration.setFrom(iv.setNew(refOf(maxIterations))),
            timeElapsed.setFrom(tev.setNew(refOf(maxSeconds))),
            finishControl.setNew(),
            tev.startTime(),
            initBlock,
            repOps.printMaxIterations(iv),
            repOps.printMaxSeconds(tev),
            repOps.printRandomSeed(),
            initReportingBlock,
            While(NOT(OR(iv.isFinished(), tev.isFinished(), finishControl.isFinished()))).Do(
                iterationBlock,
                iv.advance()
            ),
            finishBlock,
            tev.endTime(),
            repOps.printTotalSeconds(tev),
            finishReportingBlock
        );
    }
    
    public Routine initReportingBlock() { return initReportingBlock; } 
    public Routine initBlock() { return initBlock; }
    public Routine iterationBlock() { return iterationBlock; }    
    public Routine finishBlock() { return finishBlock; }
    public Routine finishReportingBlock() { return finishReportingBlock; } 
    public IterationRef iteration() { return iteration; }
    public TimeElapsedRef timeElapsed() { return timeElapsed; }
    public FinishControlRef finishControl() { return finishControl; }

    @Override
    public Procedure assemble(Context ctx)
    {
        return main.assemble(ctx);
    }
}
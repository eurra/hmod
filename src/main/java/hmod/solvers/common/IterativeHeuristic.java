
package hmod.solvers.common;

import hmod.core.AlgorithmException;
import hmod.core.ValidableRoutine;
import static hmod.core.FlowchartFactory.*;
import hmod.core.Routine;
import hmod.core.Statement;
import optefx.loader.Processable;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;

/**
 *
 * @author Enrique Urra C.
 */
public final class IterativeHeuristic
{    
    public static final Parameter<Integer> MAX_ITERATIONS = new Parameter<>("IterativeHeuristic.MAX_ITERATIONS");
    public static final Parameter<Double> MAX_SECONDS = new Parameter<>("IterativeHeuristic.MAX_SECONDS");
    
    @LoadsComponent({ 
        Heuristic.class, 
        IterativeHeuristic.class, 
        IterationHandler.class, 
        TimeElapsedHandler.class, 
        FinishHandler.class
    })
    public static void load(ComponentRegister cr, ParameterRegister pr)
    {
        int maxIteration = pr.getRequiredValue(MAX_ITERATIONS);
        double maxSeconds = pr.isValueSet(MAX_SECONDS) ? pr.getValue(MAX_SECONDS) : -1.0;
        
        MutableIterationHandler ih = cr.provide(new MutableIterationHandler(maxIteration), IterationHandler.class);
        MutableTimeElapsedHandler teh = cr.provide(new MutableTimeElapsedHandler(maxSeconds), TimeElapsedHandler.class);
        MutableFinishHandler fh = cr.provide(new MutableFinishHandler(), FinishHandler.class);
        IterativeHeuristic iHeu = cr.provide(new IterativeHeuristic(ih, teh, fh));
        
        Heuristic heu = new Heuristic()
        {
            @Override
            public void run() throws AlgorithmException
            {
                iHeu.main.run();
            }

            @Override
            public void stop() throws AlgorithmException
            {
                iHeu.main.stop();
            }
        };
        
        cr.provide(heu, Heuristic.class);
    }
    
    private final Statement main;
    private final ValidableRoutine init = new ValidableRoutine("IterativeHeuristic.init");
    private final ValidableRoutine initReporting = new ValidableRoutine("IterativeHeuristic.initReporting", false);
    private final ValidableRoutine iteration = new ValidableRoutine("IterativeHeuristic.iteration");
    private final ValidableRoutine finish = new ValidableRoutine("IterativeHeuristic.finish");
    private final ValidableRoutine finishReporting = new ValidableRoutine("IterativeHeuristic.finishReporting", false);

    private IterativeHeuristic(MutableIterationHandler ih,
                               MutableTimeElapsedHandler teh,
                               MutableFinishHandler fh)
    {
        ReportingProcesses rp = new ReportingProcesses(ih, teh);
        
        main = block(
            teh::startTime,
            ih::resetIterations,
            fh::restartFinishFlag,
            init,
            rp::printMaxIterations,
            rp::printMaxSeconds,
            ReportingProcesses::printRandomSeed,
            initReporting,
            While(NOT(OR(ih::areIterationsFinished, teh::isTimeFinished, fh::isManuallyFinished))).Do(
                iteration,
                ih::advanceIteration
            ),
            finish,
            teh::endTime,
            rp::printTotalSeconds,
            finishReporting
        );
    }

    public Routine initReporting() { return initReporting; } 
    public Routine init() { return init; }
    public Routine iteration() { return iteration; }    
    public Routine finish() { return finish; }
    public Routine finishReporting() { return finishReporting; } 
    
    @Processable
    private void validate()
    {
        init.validate();
        initReporting.validate();
        iteration.validate();
        finish.validate();
        finishReporting.validate();
    }
}


package hmod.solvers.common;

import static hmod.core.FlowchartFactory.*;
import hmod.core.Statement;
import java.util.concurrent.atomic.AtomicReference;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Module;
import optefx.loader.ModuleLoader;
import optefx.util.output.BasicManagerType;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Enrique Urra C.
 */
public class FlowchartTest
{
    public static final String OUTPUT_ID = "test-id";
    
    @Rule public ExpectedException thrown = ExpectedException.none();
    
    @BeforeClass
    public static void init()
    {
        OutputManager.setCurrent(BasicManagerType.MULTI_THREAD);
        
        OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
            addSystemOutputId(HeuristicOutputIds.EXECUTION_INFO).
            addSystemOutputId(OUTPUT_ID)
        ); 
    }
    
    @Before
    public void prevTest()
    {
        System.out.println();
    }
    
    public static class IterationData
    {
        private int currentIteration = 0;
        private final int maxIteration = 10;

        private IterationData()
        {
        }
        
        public void nextIteration()
        {
            currentIteration++;
        }
        
        public boolean isFinished()
        {
            return currentIteration >= maxIteration;
        }

        public int getCurrentIteration()
        {
            return currentIteration;
        }

        public int getMaxIteration()
        {
            return maxIteration;
        }
    }
    
    public static class DummyClass
    {
    }
    
    public static class AnotherDummyClass
    {
    }
    
    public static class ManualTestProcesses
    {
        public static void printInitMsg()
        {
            OutputManager.println(OUTPUT_ID, "hello!");
        }
       
        public static void printIterationMsg()
        {
            OutputManager.println(OUTPUT_ID, "iteration!");
        }
        
        public static void printFinishMsg()
        {
            OutputManager.println(OUTPUT_ID, "bye!");
        }
        
        public static void printRandomNumber()
        {
            OutputManager.println(OUTPUT_ID, "random number: " + RandomTool.getInt(100));
        }
        
        private final IterationData id;

        private ManualTestProcesses(IterationData id)
        {
            this.id = id;
        }
        
        public boolean isCurrentPlusRandomGreatherThanMax()
        {
            if(id.getCurrentIteration() + RandomTool.getInt(5) > id.getMaxIteration())
            {
                OutputManager.println(OUTPUT_ID, "current plus random is greather than max!");
                return true;
            }

            return false;
        }
    }
    
    private static class ManualTest
    {
        @LoadsComponent({ Heuristic.class, IterationData.class })
        public static void load(ComponentRegister cr)
        {
            IterationData id = cr.provide(new IterationData());
            ManualTestProcesses tp = new ManualTestProcesses(id);
            
            Statement init = block(
                ManualTestProcesses::printInitMsg,
                ManualTestProcesses::printRandomNumber
            );
            
            Statement iteration = block(
                ManualTestProcesses::printIterationMsg,
                id::nextIteration
            );
            
            Statement finish = block(
                ManualTestProcesses::printRandomNumber,
                ManualTestProcesses::printFinishMsg
            );
            
            Statement main = block(
                init,
                While(NOT(OR(id::isFinished, tp::isCurrentPlusRandomGreatherThanMax))).Do(
                    iteration
                ),
                finish
            );
            
            cr.provide(() -> main.run(), Heuristic.class);
        }
    }
    
    @Test
    public void manualHeuristic()
    {
        new ModuleLoader().
            load(ManualTest.class).
            getInstance(Heuristic.class).
            run();
    }
    
    @LoadsComponent
    public static void dummyLoader(IterativeHeuristic ih)
    {
        ih.init().append(() -> System.out.println("init!"));
        ih.iteration().append(() -> System.out.println("aaaaa"));
        ih.finish().append(() -> System.out.println("finish!"));
    }
    
    @Test
    public void dummyHeuristic()
    {
        Module mod = new ModuleLoader().
            load(IterativeHeuristic.class).
            load(FlowchartTest.class, "dummyLoader").
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 10).
            getModule();
        
        mod.getInstance(Heuristic.class).run();
        assertEquals(10, mod.getInstance(IterationHandler.class).getCurrentIteration());
    }
    
    
    @Test
    public void doubleLevelHeuristic()
    {
        int maxIterations = RandomTool.getInt(50);
        int testNumber = RandomTool.getInt(100);
        
        Module mod = new ModuleLoader().
            loadAll(IterativeHeuristic.class, TestHeuristic.class).
            setParameter(IterativeHeuristic.MAX_ITERATIONS, maxIterations).
            setParameter(TestHeuristic.TEST_NUMBER, testNumber).
            getModule();
        
        mod.getInstance(Heuristic.class).run();
        assertEquals(maxIterations, mod.getInstance(IterationHandler.class).getCurrentIteration());
        assertEquals(testNumber + maxIterations, mod.getInstance(TestHandler.class).getTestNumber());
        
        System.out.println("\n" + mod.getLoadLog());
    }
    
    @Test
    public void stateUsage()
    {
        ModuleLoader loader = new ModuleLoader().
            loadAll(IterativeHeuristic.class, TestHeuristic.class);
        
        Module mod = loader.
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 8).
            setParameter(TestHeuristic.TEST_NUMBER, 5).
            getModule();
        
        mod.getInstance(Heuristic.class).run();
        assertEquals(8, mod.getInstance(IterationHandler.class).getCurrentIteration());
        assertEquals(13, mod.getInstance(TestHandler.class).getTestNumber());
        System.out.println("\n");
        
        mod.getInstance(Heuristic.class).run();
        assertEquals(8, mod.getInstance(IterationHandler.class).getCurrentIteration());
        assertEquals(21, mod.getInstance(TestHandler.class).getTestNumber());
        System.out.println("\n");
        
        Module mod2 = loader.
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 5).
            setParameter(TestHeuristic.TEST_NUMBER, 345).
            getModule();
        
        mod2.getInstance(Heuristic.class).run();
        assertEquals(5, mod2.getInstance(IterationHandler.class).getCurrentIteration());
        assertEquals(350, mod2.getInstance(TestHandler.class).getTestNumber());
    }
    
    @Test
    public void onlineParameter() throws Throwable
    {
        Module mod = new ModuleLoader().
            load(IterativeHeuristic.class).
            load(TestHeuristic.class).
            setParameter(IterativeHeuristic.MAX_SECONDS, 0.5).
            setParameter(IterativeHeuristic.MAX_ITERATIONS, -1).
            setParameter(TestHeuristic.TEST_NUMBER, 345).
            getModule();
        
        AtomicReference<Throwable> exception = new AtomicReference<>();
        
        Runnable threadHandler = () -> {
            OutputManager.getCurrent().setOutputsFromConfig(new OutputConfig().
                addSystemOutputId("HeuristicOutputIds.EXECUTION_INFO").
                addSystemOutputId(OUTPUT_ID)
            );
            
            try
            {
                mod.getInstance(Heuristic.class).run();
            }
            catch(Throwable ex)
            {
                exception.set(ex);
            }
        };
        
        Thread thread = new Thread(threadHandler);        
        thread.start();
        Thread.sleep(100);
        
        mod.setParameter(TestHeuristic.TEST_TRIGGER, 0);
        OutputManager.print(OUTPUT_ID, "\n\n\n\n\n\n\n\n\n\n\n\n ****** CHANGED ****** \n\n\n\n\n\n\n\n\n\n\n\n");
        
        thread.join();
        
        if(exception.get() != null)
            throw exception.get();
    }
}

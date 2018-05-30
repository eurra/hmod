
package hmod.solvers.common;

import static hmod.core.AlgorithmFactory.*;
import hmod.core.Context;
import hmod.core.Evaluation;
import static hmod.core.MethodBridges.eval;
import static hmod.core.MethodBridges.run;
import hmod.core.NestableContext;
import hmod.core.Statement;
import hmod.core.Ref;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Module;
import optefx.loader.ModuleLoader;
import optefx.util.output.BasicManagerType;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;
import optefx.util.random.RandomTool;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author Enrique Urra C.
 */
public class AlgorithmTest
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
    
    public static class DummyClass
    {
    }
    
    public static class AnotherDummyClass
    {
    }
    
    public static class ManualTesting
    {
        public static final class Ops
        {
            public Statement printInitMsg()
            {
                return run(ManualTesting::printInitMsg);
            }
            
            public Statement printIterationMsg()
            {
                return run(ManualTesting::printIterationMsg);
            }
            
            public Statement printFinishMsg()
            {
                return run(ManualTesting::printFinishMsg);
            }
            
            public Statement printRandomNumber()
            {
                return run(ManualTesting::printRandomNumber);
            }
            
            public Evaluation<Boolean> isCurrentPlusRandomGreatherThanMax(Ref<? extends Iteration> it)
            {
                return eval(ManualTesting::isCurrentPlusRandomGreatherThanMax, it);                
            }
        }
        
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
        
        public static boolean isCurrentPlusRandomGreatherThanMax(Iteration it)
        {
            if(it.getCurrent()+ RandomTool.getInt(5) > it.getMax())
            {
                OutputManager.println(OUTPUT_ID, "current plus random is greather than max!");
                return true;
            }

            return false;
        }
    }
    
    private static class ManualTest
    {
        @LoadsComponent(IterativeHeuristic.class)
        public static void load(ComponentRegister cr, IterativeHeuristic ih)
        {
            ManualTesting.Ops mOps = new ManualTesting.Ops();
            
            ih.initBlock().append(block("ManualTest.init",
                mOps.printInitMsg(),
                mOps.printRandomNumber()
            ));
            
            ih.iterationBlock().append(block("ManualTest.iteration",
                mOps.printIterationMsg(),
                If(mOps.isCurrentPlusRandomGreatherThanMax(ih.iteration())).then(
                    ih.finishControl().triggerFinish()
                )
            ));
            
            ih.finishBlock().append(block("ManualTest.finish",
                mOps.printRandomNumber(),
                mOps.printFinishMsg()
            ));
        }
    }
    
    @Test
    public void manualHeuristic()
    {
        new ModuleLoader().
            loadAll(IterativeHeuristic.class, ManualTest.class).
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 10).
            getInstance(Heuristic.class).
            assemble().run();
    }
    
    @LoadsComponent
    public static void dummyLoader(IterativeHeuristic ih)
    {
        ih.initBlock().append(run(()-> System.out.println("init!")));
        ih.iterationBlock().append(run(() -> System.out.println("aaaaa")));
        ih.finishBlock().append(run(() -> System.out.println("finish!")));
    }
    
    @Test
    public void dummyHeuristic()
    {
        IterativeHeuristic ih = new ModuleLoader().
            load(IterativeHeuristic.class).
            load(AlgorithmTest.class, "dummyLoader").
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 10).
            getInstance(IterativeHeuristic.class);
        
        Context ctx = new NestableContext().declare(ih.iteration());
        
        ih.assemble(ctx).run();
        assertEquals(10, ih.iteration().getFrom(ctx).getCurrent());
    }
    
    
    @Test
    public void doubleLevelHeuristic()
    {
        int maxIterations = RandomTool.getInt(50);
        int testNumber = RandomTool.getInt(100);
        
        Module mod = new ModuleLoader().
            loadAll(IterativeHeuristic.class, TestHeuristic.class).
            setParameter(IterativeHeuristic.MAX_ITERATIONS, maxIterations).
            setParameter(TestHeuristic.NUM, testNumber).
            getModule();
        
        IterativeHeuristic ih = mod.getInstance(IterativeHeuristic.class);
        TestHeuristic th = mod.getInstance(TestHeuristic.class);
        Context ctx = new NestableContext().declare(ih.iteration(), th.testNumber());
        
        ih.assemble(ctx).run();        
        assertEquals(maxIterations, ih.iteration().getFrom(ctx).getCurrent());
        assertEquals(testNumber + maxIterations, th.testNumber().getFrom(ctx).getTestNumber());
    }
    
    @Test
    public void stateUsage()
    {
        ModuleLoader loader = new ModuleLoader().
            loadAll(IterativeHeuristic.class, TestHeuristic.class);
        
        Module mod = loader.
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 8).
            setParameter(TestHeuristic.NUM, 5).
            getModule();
        
        IterativeHeuristic ih = mod.getInstance(IterativeHeuristic.class);
        TestHeuristic th = mod.getInstance(TestHeuristic.class);
        Context ctx = new NestableContext().declare(ih.iteration(), th.testNumber());
        
        ih.assemble(ctx).run();        
        assertEquals(8, ih.iteration().getFrom(ctx).getCurrent());
        assertEquals(13, th.testNumber().getFrom(ctx).getTestNumber());
        System.out.println("\n");
        
        Module mod2 = loader.
            setParameter(IterativeHeuristic.MAX_ITERATIONS, 5).
            setParameter(TestHeuristic.NUM, 345).
            getModule();
        
        ih = mod2.getInstance(IterativeHeuristic.class);
        th = mod2.getInstance(TestHeuristic.class);
        ctx = new NestableContext().declare(ih.iteration(), th.testNumber());
        
        ih.assemble(ctx).run(); 
        assertEquals(5, ih.iteration().getFrom(ctx).getCurrent());
        assertEquals(350, th.testNumber().getFrom(ctx).getTestNumber());
    }
}

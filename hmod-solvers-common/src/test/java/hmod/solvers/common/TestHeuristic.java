
package hmod.solvers.common;

import static hmod.core.AlgorithmFactory.block;
import static hmod.core.AlgorithmFactory.refOf;
import static hmod.core.MethodBridges.set;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;


/**
 *
 * @author Enrique Urra C.
 */
public final class TestHeuristic
{
    public static final Parameter<Integer> NUM = new Parameter<>("TestHeuristic.NUM");
    
    @LoadsComponent({ IterativeHeuristic.class, TestHeuristic.class })
    public static void load(ComponentRegister cr, ParameterRegister pr, IterativeHeuristic ih)
    {
        int number = pr.getRequiredValue(NUM);
        TestHeuristic th = cr.provide(new TestHeuristic());
        TestingOps testOps = TestingOps.getInstance();
        
        ih.initBlock().append(block("TestHeuristic.init",
            set(th.testNumber, TestNumber::new, refOf(number)),
            testOps.testInit()
        ));
        
        ih.iterationBlock().append(block("TestHeuristic.iteration",
            testOps.test(th.testNumber)
        ));
        
        ih.finishBlock().append(block("TestHeuristic.finish",
            testOps.testFinish()
        ));
    }
    
    private final TestNumberVar testNumber = new TestNumberVar();
    
    private TestHeuristic()
    {
    }

    public TestNumberVar testNumber() { return testNumber; }
}

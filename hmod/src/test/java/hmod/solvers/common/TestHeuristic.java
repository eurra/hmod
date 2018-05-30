
package hmod.solvers.common;

import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;
import optefx.loader.Parameter;
import optefx.loader.ParameterRegister;
import optefx.loader.Trigger;


/**
 *
 * @author Enrique Urra C.
 */
public final class TestHeuristic
{
    public static final Parameter<Integer> TEST_NUMBER = new Parameter<>("TestHeuristic.TEST_NUMBER");
    public static final Trigger<Integer> TEST_TRIGGER = new Trigger<>("TestHeuristic.TEST_TRIGGER");
    
    @LoadsComponent({ IterativeHeuristic.class, TestHandler.class })
    public static void load(IterativeHeuristic ih, ComponentRegister cr, ParameterRegister pr)
    {
        int testNumber = pr.getRequiredValue(TEST_NUMBER);
        TestHandler th = cr.provide(new TestHandler(testNumber));
        TestOperators to = new TestOperators(th);
        
        ih.init().append(TestOperators::testInit);
        ih.iteration().append(to::test);
        ih.finish().append(TestOperators::testFinish);
        
        pr.addListener(TEST_TRIGGER, (value) -> th.setTestNumber(value));
    }
}

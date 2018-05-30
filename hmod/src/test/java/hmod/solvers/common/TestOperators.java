
package hmod.solvers.common;

import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public class TestOperators
{
    public static void testInit()
    {
        OutputManager.println(FlowchartTest.OUTPUT_ID, "Initializing heuristic!");
    }
    
    public static void testFinish()
    {
        OutputManager.println(FlowchartTest.OUTPUT_ID, "Finishing heuristic!");
    }
    
    private final TestHandler testHandler;

    TestOperators(TestHandler testHandler)
    {
        this.testHandler = testHandler;
    }
    
    public void test()
    {
        int prueba = testHandler.getTestNumber();
        String elemsStr = " (test existe!, prev: " + prueba + ")";
        testHandler.incrementNumber();
        OutputManager.println(FlowchartTest.OUTPUT_ID, "Prueba!" + elemsStr);
    }
}

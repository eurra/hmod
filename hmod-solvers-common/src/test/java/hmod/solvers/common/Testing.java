
package hmod.solvers.common;

import hmod.ap.FindOperations;
import hmod.ap.Operator;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public class Testing
{
    @Operator
    public static void testInit()
    {
        OutputManager.println(AlgorithmTest.OUTPUT_ID, "Initializing heuristic!");
    }
    
    @Operator
    public static void testFinish()
    {
        OutputManager.println(AlgorithmTest.OUTPUT_ID, "Finishing heuristic!");
    }
    
    @Operator
    public static void test(TestNumber test)
    {
        int prueba = test.getTestNumber();
        String elemsStr = " (test existe!, prev: " + prueba + ")";
        test.incrementNumber();
        OutputManager.println(AlgorithmTest.OUTPUT_ID, "Prueba!" + elemsStr);
    }
}

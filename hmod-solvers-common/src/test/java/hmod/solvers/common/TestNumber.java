
package hmod.solvers.common;

import hmod.ap.FindOperations;
import hmod.ap.Operator;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public class TestNumber
{    
    private int testNumber;

    public TestNumber(int number)
    {
        this.testNumber = number;
    }
 
    @Operator
    public int getTestNumber()
    {
        return testNumber;
    }

    @Operator
    public void incrementNumber()
    {
        testNumber++;
    }
}

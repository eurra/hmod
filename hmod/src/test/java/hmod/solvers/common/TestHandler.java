
package hmod.solvers.common;

/**
 *
 * @author Enrique Urra C.
 */
public class TestHandler
{
    private int testNumber;

    TestHandler(int testNumber)
    {
        this.testNumber = testNumber;
    }

    public void setTestNumber(int testNumber)
    {
        this.testNumber = testNumber;
    }

    public int getTestNumber()
    {
        return testNumber;
    }

    public void incrementNumber()
    {
        testNumber++;
    }
}

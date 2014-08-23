
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class NORCondition implements Condition
{
    private Condition toTest;

    public void setDecider(Condition decider)
    {
        this.toTest = decider;
    }

    @Override
    public Boolean evaluate() throws AlgorithmException
    {
        return toTest != null && !toTest.evaluate();
    }
}

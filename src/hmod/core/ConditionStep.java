
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class ConditionStep implements Step
{
    private Condition condition;
    private Step nextTrue;
    private Step nextFalse;

    public void setCondition(Condition condition)
    {
        this.condition = condition;
    }

    public void setNextTrue(Step nextTrue)
    {
        this.nextTrue = nextTrue;
    }
    
    public void setNextFalse(Step nextFalse)
    {
        this.nextFalse = nextFalse;
    }

    @Override
    public final Step resolveNext() throws AlgorithmException
    {
        if(condition != null && condition.evaluate())
            return nextTrue;
        else
            return nextFalse;
    }
}


package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class DirectStep implements Step
{
    private Operator operator;
    private Step next;

    public void setNextStep(Step next)
    {
        this.next = next;
    }
    
    public void setOperator(Operator operator)
    {
        this.operator = operator;
    }

    @Override
    public Step resolveNext() throws AlgorithmException
    {
        if(operator != null)
            operator.execute();
        
        return next;
    }
}

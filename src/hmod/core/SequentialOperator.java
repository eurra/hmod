
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class SequentialOperator implements Operator
{
    private Operator[] operators;

    public void setOperators(Operator[] operators)
    {
        if(operators == null)
            throw new NullPointerException("Null operators");
        
        for(int i = 0; i < operators.length; i++)
        {
            if(operators[i] == null)
                throw new NullPointerException("Null operator at position " + i);
        }
        
        this.operators = operators;
    }

    @Override
    public void execute() throws AlgorithmException
    {
        if(operators != null)
        {
            for(int i = 0; i < operators.length; i++)
                operators[i].execute();
        }
    }
}

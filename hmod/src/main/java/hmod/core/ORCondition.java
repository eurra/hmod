
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class ORCondition extends MultiExpression<Boolean> implements Condition
{
    public ORCondition(Expression<Boolean>... conditions)
    {
        super(conditions);
    }

    @Override
    public Boolean get() throws AlgorithmException
    {
        int count = getChildsCount();
        
        for(int i = 0; i < count; i++)
        {
            if(getChildAt(i).get())
                return true;
        }

        return false;
    }
}

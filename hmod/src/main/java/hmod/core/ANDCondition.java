
package hmod.core;

import java.util.function.Supplier;

/**
 *
 * @author Enrique Urra C.
 */
public final class ANDCondition extends MultiExpression<Boolean> implements Condition
{
    public ANDCondition(Expression<Boolean>... conditions)
    {
        super(conditions);
    }

    @Override
    public Boolean get() throws AlgorithmException
    {
        int count = getChildsCount();
        
        for(int i = 0; i < count; i++)
        {
            if(!getChildAt(i).get())
                return false;
        }

        return true;
    }
}

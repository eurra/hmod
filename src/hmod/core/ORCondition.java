
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public final class ORCondition extends MultipleEvaluator<Boolean> implements Condition
{
    @Override
    public Boolean evaluate() throws AlgorithmException
    {
        int count = getEvaluatorsCount();
        
        for(int i = 0; i < count; i++)
        {
            if(getEvaluatorAt(i).evaluate())
                return true;
        }

        return false;
    }
}

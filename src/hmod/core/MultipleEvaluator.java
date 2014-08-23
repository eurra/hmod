
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class MultipleEvaluator<T> implements Evaluator<T>
{
    private Evaluator<T>[] evaluators;

    public final void setEvaluators(Evaluator<T>[] evaluators)
    {
        if(evaluators == null)
            throw new NullPointerException("Null evaluators array");
        
        Evaluator[] newEvaluators = new Evaluator[evaluators.length];
        
        for(int i = 0; i < evaluators.length; i++)
        {
            if(evaluators[i] == null)
                throw new NullPointerException("Null evaluator at position " + i);
            
            newEvaluators[i] = evaluators[i];
        }
        
        this.evaluators = newEvaluators;
    }
    
    public final int getEvaluatorsCount()
    {
        if(evaluators == null)
            return 0;
        
        return evaluators.length;
    }
    
    public final Evaluator<T> getEvaluatorAt(int pos) throws IndexOutOfBoundsException
    {        
        if(evaluators == null || pos < 0 || pos >= evaluators.length)
            throw new IndexOutOfBoundsException("Wrong evaluator position: " + pos);
        
        return evaluators[pos];
    }
}

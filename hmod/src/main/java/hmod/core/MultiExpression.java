
package hmod.core;

import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class MultiExpression<T> implements NestableExpression<T>
{
    private final Expression<T>[] evaluators;

    public MultiExpression(Expression<T>... evaluators)
    {
        this.evaluators = new Expression[evaluators.length];
        
        for(int i = 0; i < evaluators.length; i++)
            this.evaluators[i] = Objects.requireNonNull(evaluators[i], "null condition at position " + i);
    }

    @Override
    public int getChildsCount()
    {
        return evaluators.length;
    }

    @Override
    public Expression<T> getChildAt(int pos) throws IndexOutOfBoundsException
    {
        return evaluators[pos];
    }
}

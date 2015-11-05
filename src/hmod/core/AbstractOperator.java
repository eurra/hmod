
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class AbstractOperator<T> implements TypedOperator<T>
{
    @Override
    public final void execute() throws AlgorithmException
    {
        evaluate();
    }
}

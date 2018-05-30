
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public interface NestableExpression<T> extends Expression<T>
{
    Expression<T> getChildAt(int pos) throws IndexOutOfBoundsException;
    int getChildsCount();
}

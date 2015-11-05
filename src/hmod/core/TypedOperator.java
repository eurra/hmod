
package hmod.core;

/**
 *
 * @author Enrique Urra C.
 */
public interface TypedOperator<T> extends Operator
{
    T evaluate() throws AlgorithmException;
}

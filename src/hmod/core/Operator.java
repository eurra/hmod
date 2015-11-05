
package hmod.core;

/**
 * Defines an operator, which can perform an specific task related to the 
 * execution flow of a heuristic.
 * @author Enrique Urra C.
 */
public interface Operator
{
    /**
     * Executes the operation.
     * @return A boolean result of the operation.
     * @throws hmod.core.AlgorithmException
     */
    void execute() throws AlgorithmException;
}

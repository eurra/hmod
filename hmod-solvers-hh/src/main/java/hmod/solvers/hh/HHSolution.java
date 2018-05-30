
package hmod.solvers.hh;

/**
 * The base and default high-level solution representation.
 * @author Enrique Urra C.
 */
public interface HHSolution<T extends HHSolution<T>> extends Comparable<T>
{
    /**
     * Gets the evaluation value for this solution. Such value MUST be positive.
     * Note that this value is NOT comparable, because for a minimization 
     * problem, an higher evaluation may represents a worse solution. For 
     * comparison, use the compareTo() method.
     * @return The evaluation value.
     */
    double getEvaluation();
    
    /**
     * Returns a number below zero, zero, or a number greater tan zero if this
     * solution is worser, equal or better than the one in the otherSolution 
     * parameter, respectively. This method must be consistent with the related 
     * problem: for a maximization and minimization problem, an higher 
     * evaluation value must represent a better and worser solution, 
     * respectively.
     * @param t The solution to compare with.
     * @return a number below zero, zero, or a number greater tan zero if this
     * solution is worser, equal or better than the compared solution.
     */
    @Override
    int compareTo(T otherSolution);
    
    default String evaluationToString()
    {
        return Double.toString(getEvaluation());
    }
    
    default boolean isSameTo(T otherSolution)
    {
        return compareTo(otherSolution) == 0;
    }
}

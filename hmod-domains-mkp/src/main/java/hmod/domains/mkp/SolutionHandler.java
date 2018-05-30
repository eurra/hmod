
package hmod.domains.mkp;

/**
 *
 * @author Enrique Urra C.
 * @param <T>
 */
public interface SolutionHandler<T extends MKPSolution>
{
    void provideSolution(T solution);
    T retrieveSolution() throws IllegalStateException;
    T getBestSolution() throws IllegalStateException;
    boolean isSolutionProvided();
}


package hmod.solvers.hh;

/**
 *
 * @author Enrique Urra C.
 * @param <T>
 */
public interface HHSolutionHandler<T extends HHSolution>
{
    T getInputSolution();
    void setOutputSolution(T solution);
    T getOutputSolution();
}


package hmod.solvers.common;

/**
 *
 * @author Enrique Urra C.
 */
public interface IterationHandler
{
    int getCurrentIteration();
    int getMaxIteration();
    boolean areIterationsFinished();
}


package hmod.solvers.hh.models.attr;

/**
 *
 * @author Enrique Urra C.
 */
public interface SolutionPool<T extends HHAttributiveSolution>
{
    int getCurrentCount();
    T getSolutionAt(int pos) throws IndexOutOfBoundsException;
}

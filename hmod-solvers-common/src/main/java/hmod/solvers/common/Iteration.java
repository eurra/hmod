
package hmod.solvers.common;

import hmod.ap.FindOperations;
import hmod.ap.Operator;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public interface Iteration
{
    int getCurrent();
    int getMax();
    @Operator boolean isFinished();
}

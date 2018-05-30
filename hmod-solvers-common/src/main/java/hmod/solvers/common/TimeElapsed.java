
package hmod.solvers.common;

import hmod.ap.FindOperations;
import hmod.ap.Operator;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public interface TimeElapsed
{
    @Operator double getElapsedSeconds();
    double getMaxSeconds();
    double getFinalSeconds();
    @Operator boolean isFinished();
}

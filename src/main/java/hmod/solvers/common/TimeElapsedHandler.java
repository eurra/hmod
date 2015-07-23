
package hmod.solvers.common;

/**
 *
 * @author Enrique Urra C.
 */
public interface TimeElapsedHandler
{
    double getElapsedSeconds();
    double getMaxSeconds();
    double getFinalSeconds();
    boolean isTimeFinished();
}

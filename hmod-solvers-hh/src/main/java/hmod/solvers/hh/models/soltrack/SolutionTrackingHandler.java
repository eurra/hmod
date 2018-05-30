
package hmod.solvers.hh.models.soltrack;

/**
 *
 * @author Enrique Urra C.
 */
public interface SolutionTrackingHandler
{
    int getGlobalNoImproveIterations();
    int getLastGlobalImproveIteration();
    int getLocalNoImproveIterations();
    int getLastLocalImproveIteration();
}

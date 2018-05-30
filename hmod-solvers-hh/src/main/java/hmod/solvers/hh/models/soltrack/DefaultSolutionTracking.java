
package hmod.solvers.hh.models.soltrack;

import hmod.solvers.common.IterationHandler;
import hmod.solvers.hh.models.selection.DomainBarrier;
import optefx.loader.ComponentRegister;
import optefx.loader.LoadsComponent;

/**
 *
 * @author Enrique Urra C.
 */
public final class DefaultSolutionTracking
{
    @LoadsComponent({
        UpdatableTrackingHandler.class,
        TrackableSolutionHandler.class,
        SolutionTrackingProcesses.class
    })
    public static void loadData(ComponentRegister cr,
                                IterationHandler ih,
                                DomainBarrier db)
    {
        TrackableSolutionHandler sh = cr.provide(new TrackableSolutionHandler<>());
        UpdatableTrackingHandler th = cr.provide(new UpdatableTrackingHandler(sh, ih));
        cr.provide(new SolutionTrackingProcesses(sh, th, ih));
        
        db.callHeuristic().prependAfter(th::updateImprovementData);
    }
}

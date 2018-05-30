
package hmod.solvers.hh.models.soltrack;

import hmod.solvers.common.IterationHandler;
import java.util.Objects;

/**
 *
 * @author Enrique Urra C.
 */
public final class UpdatableTrackingHandler implements SolutionTrackingHandler
{
    private final TrackableSolutionHandler sh;
    private final IterationHandler ih;
    private int lastGlobalImproveIteration;
    private int lastLocalImproveIteration;

    public UpdatableTrackingHandler(TrackableSolutionHandler sh,
                                    IterationHandler ih)
    {
        this.sh = Objects.requireNonNull(sh, "null solution handler");
        this.ih = Objects.requireNonNull(ih, "null iteration handler");
    }

    @Override
    public int getLastGlobalImproveIteration()
    {
        return lastGlobalImproveIteration;
    }

    @Override
    public int getGlobalNoImproveIterations()
    {
        return ih.getCurrentIteration()- lastGlobalImproveIteration;
    }
    
    @Override
    public int getLastLocalImproveIteration()
    {
        return lastLocalImproveIteration;
    }
    
    @Override
    public int getLocalNoImproveIterations()
    {
        return ih.getCurrentIteration()- lastLocalImproveIteration;
    }
    
    /**
     * Updates the information related to the improvement of the current local 
     * and global optimum.
     */
    public void updateImprovementData()
    {
        if(sh.outputImprovedInput())
            lastLocalImproveIteration = ih.getCurrentIteration();

        if(sh.outputImprovedBest())
            lastGlobalImproveIteration = ih.getCurrentIteration();
    }
}

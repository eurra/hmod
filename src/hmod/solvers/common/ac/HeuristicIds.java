
package hmod.solvers.common.ac;

import static flexbuilders.graph.GraphFactory.nodeId;
import flexbuilders.graph.NodeLoader;
import flexbuilders.graph.NodeId;
import hmod.core.Step;

/**
 *
 * @author Enrique Urra C.
 */
public final class HeuristicIds
{
    private HeuristicIds()
    {
    }
    
    public static final NodeId<Integer> MAX_ITERATION_CONFIG = nodeId();
    public static final NodeId<Double> MAX_SECONDS_CONFIG = nodeId();
    
    public static final NodeLoader<IterationHandler> ITERATION_DATA = DataLoaders::iterationDataLoad;
    public static final NodeLoader<TimeElapsedHandler> TIME_ELAPSED_DATA = DataLoaders::timeElapsedDataLoad;
    public static final NodeLoader<FinishHandler> FINISH_DATA = DataLoaders::finishDataLoad;
    
    public static final NodeLoader<Step> MAIN_START = HeuristicStartLoader::load;
    public static final NodeId<Step> INIT_START = nodeId();
    public static final NodeId<Step> ITERATION_START = nodeId();
    public static final NodeId<Step> FINISH_START = nodeId();
}

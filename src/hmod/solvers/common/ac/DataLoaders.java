
package hmod.solvers.common.ac;

import static flexbuilders.basic.BasicBuilders.builderFor;
import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;
import flexbuilders.graph.BuilderGraph;
import flexbuilders.graph.NodePriority;
import static hmod.loader.graph.AlgorithmBuilders.argFactory;

/**
 *
 * @author Enrique Urra C.
 */
class DataLoaders
{    
    public static NestedBuilder<IterationHandler> iterationDataLoad(BuilderGraph graph) throws BuildException
    {
        HeuristicHandlerFactory handlers = HeuristicHandlerFactory.getInstance();
        NestedBuilder maxIt = graph.setValue(HeuristicIds.MAX_ITERATION_CONFIG, builderFor(-1), NodePriority.DEFAULT_VALUE);

        return argFactory(handlers::iteration, maxIt);
    }
    
    public static NestedBuilder<TimeElapsedHandler> timeElapsedDataLoad(BuilderGraph graph) throws BuildException
    {
        HeuristicHandlerFactory handlers = HeuristicHandlerFactory.getInstance();
        NestedBuilder maxTime = graph.setValue(HeuristicIds.MAX_SECONDS_CONFIG, builderFor(-1.0), NodePriority.DEFAULT_VALUE);

        return argFactory(handlers::timeElapsed, maxTime);
    }
    
    public static NestedBuilder<FinishHandler> finishDataLoad(BuilderGraph graph) throws BuildException
    {
        HeuristicHandlerFactory handlers = HeuristicHandlerFactory.getInstance();
        return argFactory(handlers::finish);
    }
}

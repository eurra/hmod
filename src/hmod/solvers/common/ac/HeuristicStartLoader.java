
package hmod.solvers.common.ac;

import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;
import flexbuilders.graph.BuilderGraph;
import hmod.core.Step;
import static hmod.loader.graph.AlgorithmBuilders.*;

/**
 *
 * @author Enrique Urra C.
 */
class HeuristicStartLoader
{
    public static NestedBuilder<Step> load(BuilderGraph graph) throws BuildException
    {
        NestedBuilder iterationData = graph.loadNode(HeuristicIds.ITERATION_DATA);
        NestedBuilder timeElapsedData = graph.loadNode(HeuristicIds.TIME_ELAPSED_DATA);
        NestedBuilder finishData = graph.loadNode(HeuristicIds.FINISH_DATA);
        HeuristicOperatorFactory operators = HeuristicOperatorFactory.getInstance();
        
        return algorithmBlock().
            run(operators::startElapsedTime, timeElapsedData).
            call(graph.getNode(HeuristicIds.INIT_START)).
            While(AND(
                NOT(condition(operators::checkIterationsFinished, iterationData)),
                NOT(condition(operators::checkTimeFinished, timeElapsedData)),
                NOT(condition(operators::checkFinished, finishData))
            )).
                call(graph.getNode(HeuristicIds.ITERATION_START)).
                run(operators::nextIteration, iterationData).
            end().
            call(graph.getNode(HeuristicIds.FINISH_START));
    }
}

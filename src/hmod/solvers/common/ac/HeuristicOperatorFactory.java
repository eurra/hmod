
package hmod.solvers.common.ac;

import hmod.core.Condition;
import hmod.core.Operator;
import hmod.loader.graph.ArgumentList;
import hmod.loader.graph.ArgumentInfo;

/**
 *
 * @author Enrique Urra C.
 */
public final class HeuristicOperatorFactory
{
    private static HeuristicOperatorFactory instance;

    public static HeuristicOperatorFactory getInstance()
    {
        if(instance == null)
            instance = new HeuristicOperatorFactory();
        
        return instance;
    }

    private HeuristicOperatorFactory()
    {
    }
    
    @ArgumentInfo(type = TimeElapsedHandler.class)
    public Operator startElapsedTime(ArgumentList list)
    {
        TimeElapsedHandler timeElapsedHandler = list.next(TimeElapsedHandler.class);
        
        return () -> {
            timeElapsedHandler.startElapsedTime();
        };
    }
    
    @ArgumentInfo(type = IterationHandler.class)
    public Condition checkIterationsFinished(ArgumentList list)
    {
        IterationHandler iterationHandler = list.next(IterationHandler.class);
        
        return () -> {
            return iterationHandler.isIterationsFinished();
        };
    }
    
    @ArgumentInfo(type = TimeElapsedHandler.class)
    public Condition checkTimeFinished(ArgumentList list)
    {
        TimeElapsedHandler timeElapsedHandler = list.next(TimeElapsedHandler.class);
        
        return () -> {
            return timeElapsedHandler.isElapsedTimeFinished();
        };
    }
    
    @ArgumentInfo(type = FinishHandler.class)
    public Condition checkFinished(ArgumentList list)
    {
        FinishHandler finishHandler = list.next(FinishHandler.class);
        
        return () -> {
            return finishHandler.isFinished();
        };
    }
    
    @ArgumentInfo(type = IterationHandler.class)
    public Operator nextIteration(ArgumentList list)
    {
        IterationHandler iterationHandler = list.next(IterationHandler.class);
        
        return () -> {
            iterationHandler.advanceIteration();
        };
    }
}

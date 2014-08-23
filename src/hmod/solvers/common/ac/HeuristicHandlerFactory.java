
package hmod.solvers.common.ac;

import hmod.loader.graph.ArgumentFactory;
import hmod.loader.graph.ArgumentList;
import hmod.loader.graph.ArgumentInfo;

/**
 *
 * @author Enrique Urra C.
 */
public final class HeuristicHandlerFactory
{
    private static HeuristicHandlerFactory instance;

    public static HeuristicHandlerFactory getInstance()
    {
        if(instance == null)
            instance = new HeuristicHandlerFactory();
        
        return instance;
    }

    private HeuristicHandlerFactory()
    {
    }
    
    @ArgumentInfo(type = Integer.class, optional = true)
    public IterationHandler iteration(ArgumentList args)
    {
        int maxIterations = args.next(Integer.class, 1);
        return new DefaultIterationHandler(maxIterations);
    }
    
    @ArgumentFactory
    public FinishHandler finish(ArgumentList args)
    {
        return new DefaultFinishHandler();
    }
    
    @ArgumentInfo(type = Double.class)
    public TimeElapsedHandler timeElapsed(ArgumentList args)
    {
        double maxTime = args.next(Double.class);
        return new DefaultTimeElapsedHandler(maxTime);
    }
}

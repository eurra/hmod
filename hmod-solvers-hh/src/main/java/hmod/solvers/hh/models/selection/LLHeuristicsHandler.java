
package hmod.solvers.hh.models.selection;

import hmod.core.Statement;
import java.util.Objects;
import optefx.util.metadata.MetadataManager;

/**
 *
 * @author Enrique Urra C.
 */
public interface LLHeuristicsHandler
{
    public static LLHeuristicsHandler create(Statement initHeuristic, Statement finishHeuristic, Statement... heuristics)
    {
        Objects.requireNonNull(initHeuristic, "null init heuristic");
        Objects.requireNonNull(finishHeuristic, "null finish heuristic");
        
        Statement[] finalHeuristics = new Statement[heuristics.length];
        
        for(int i = 0; i < heuristics.length; i++)
            finalHeuristics[i] = Objects.requireNonNull(heuristics[i], "null heuristic at position " + i);
        
        return new LLHeuristicsHandler()
        {
            @Override
            public int getHeuristicsCount()
            {
                return finalHeuristics.length;
            }

            @Override
            public Statement getHeuristicAt(int index) throws IndexOutOfBoundsException
            {
                return finalHeuristics[index];
            }

            @Override
            public Statement getInitializerHeuristic()
            {
                return initHeuristic;
            }

            @Override
            public Statement getFinisherHeuristic()
            {
                return finishHeuristic;
            }
        };
    }
    
    int getHeuristicsCount();
    Statement getHeuristicAt(int index) throws IndexOutOfBoundsException;
    Statement getInitializerHeuristic();
    Statement getFinisherHeuristic();
    
    default LowLevelHeuristicInfo getInfoAt(int index) throws IndexOutOfBoundsException
    {
        Statement st = getHeuristicAt(index);
        return MetadataManager.getInstance().getDataFor(st, LowLevelHeuristicInfo.class);
    }
}

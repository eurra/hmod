
package hmod.solvers.hh.models.attr;

import hmod.core.Statement;
import hmod.solvers.hh.models.selection.LLHeuristicsHandler;
import java.util.HashMap;

/**
 *
 * @author Enrique Urra C.
 */
public class HeuristicScoringHandler<T>
{
    private class HeuristicStats
    {
        public int readingCount;
        public double changeImpactTotal;
    }
    
    private HashMap<Statement, HeuristicStats> stats;

    HeuristicScoringHandler(LLHeuristicsHandler hh)
    {
        if(hh == null)
            throw new NullPointerException("Null heuristic handler");
        
        stats = new HashMap<>(hh.getHeuristicsCount());
        int count = hh.getHeuristicsCount();
        
        for(int i = 0; i < count; i++)
            stats.put(hh.getHeuristicAt(i), new HeuristicStats());
    }
    
    public void addReadingFor(Statement heuristic, AttributesCollection<T> beforeAttrs, AttributesCollection<T> afterAttrs)
    {
        int attrsOnBoth = 0;
        
        for(T attr : beforeAttrs)
        {
            if(afterAttrs.hasAttribute(attr))
                attrsOnBoth++;
        }

        double resRate = 1.0 - ((double)attrsOnBoth / (double)(afterAttrs.getAttributesCount() + beforeAttrs.getAttributesCount() - attrsOnBoth));
        addReadingFor(heuristic, resRate);
    }
    
    public void addReadingFor(Statement heuristic, double reading)
    {
        if(!stats.containsKey(heuristic))
            throw new IllegalArgumentException("The provided heuristic do no belongs to this collection");
        
        HeuristicStats stat = stats.get(heuristic);
        stat.changeImpactTotal += reading;
        stat.readingCount++;
    }
    
    public double getCurrentScore(Statement heuristic)
    {
        if(!stats.containsKey(heuristic))
            throw new IllegalArgumentException("The provided heuristic do no belongs to this collection");
        
        HeuristicStats stat = stats.get(heuristic);
        
        if(stat.readingCount == 0.0)
            return 0.5;
        
        return stat.changeImpactTotal / (double)stat.readingCount;
    }
    
    public boolean hasScore(Statement heuristic)
    {
        return stats.containsKey(heuristic);
    }
}

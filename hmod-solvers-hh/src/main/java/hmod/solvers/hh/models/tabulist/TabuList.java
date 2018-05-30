
package hmod.solvers.hh.models.tabulist;

import hmod.solvers.hh.HHSolution;
import java.util.LinkedList;

/**
 *
 * @author Enrique Urra C.
 */
public class TabuList<T extends HHSolution>
{
    private final LinkedList<T> tabuList = new LinkedList<>();
    private final int tabuListSize;

    public TabuList(int tabuListSize)
    {
        if(tabuListSize <= 0)
            throw new IllegalArgumentException("illegal size: " + tabuListSize);
        
        this.tabuListSize = tabuListSize;
    }
    
    public final boolean tryQueue(T solution)
    {
        if(tabuListSize > 0)
        {
            if(contains(solution))
                return false;
            
            if(tabuList.size() == tabuListSize)
                tabuList.removeLast();

            tabuList.addFirst(solution);
            return true;
        }
        
        return false;
    }
    
    public final boolean contains(T solution)
    {
        for(T currSolution : tabuList)
        {
            if(currSolution.isSameTo(solution))
                return true;
        }
        
        return false;
    }
}

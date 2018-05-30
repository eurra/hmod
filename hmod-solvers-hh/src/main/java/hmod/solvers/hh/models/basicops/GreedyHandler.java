
package hmod.solvers.hh.models.basicops;

import hmod.solvers.hh.HHSolution;

/**
 *
 * @author Enrique Urra C.
 */
class GreedyHandler
{
    private HHSolution currBest;

    public GreedyHandler()
    {
    }
    
    public void resetGreedyData()
    {
        currBest = null;
    }

    public void registerSolution(HHSolution solution)
    {
        if(currBest == null || currBest.compareTo(solution) < 0)
            currBest = solution;
    }

    public HHSolution getBestSolution()
    {
        return currBest;
    }
}

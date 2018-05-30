
package hmod.solvers.hh.adapters.darptw;

import hmod.domains.darptw.DARPTWSolution;
import hmod.solvers.hh.HHSolution;

public class DARPTWHHSolution<T extends DARPTWHHSolution<T>> implements HHSolution<T>
{
    private final DARPTWSolution innerSolution;

    public DARPTWHHSolution(DARPTWSolution innerSolution)
    {
        this.innerSolution = innerSolution;
    }

    public DARPTWSolution getInnerSolution()
    {
        return innerSolution;
    }
    
    @Override
    public double getEvaluation()
    {
        return innerSolution.getFinalScore();
    }

    @Override
    public int compareTo(DARPTWHHSolution otherSolution)
    {
        double thisEval = innerSolution.getFinalScore();
        double otherEval = otherSolution.innerSolution.getFinalScore();
        
        if(thisEval < otherEval)
            return 1;
        else if(thisEval == otherEval) 
            return 0;
        else
            return -1;
    }

    @Override
    public String toString()
    {
        return innerSolution.toString();
    }
}


package hmod.solvers.hh.adapters.binary;

import hmod.domains.binary.BinarySolution;
import hmod.solvers.hh.HHSolution;

public class HHBinarySolution implements HHSolution<HHBinarySolution>
{
    private final BinarySolution innerSolution;

    public HHBinarySolution(BinarySolution innerSolution)
    {
        this.innerSolution = innerSolution;
    }

    public BinarySolution getInnerSolution()
    {
        return innerSolution;
    }
    
    @Override
    public double getEvaluation()
    {
        return innerSolution.fitness();
    }
    
    @Override
    public int compareTo(HHBinarySolution otherSolution)
    {
        double thisFitness = innerSolution.fitness();
        double otherFitness = otherSolution.innerSolution.fitness();
        
        if(thisFitness > otherFitness)
            return 1;
        else if(thisFitness == otherFitness)
            return 0;
        else
            return -1;
    }
    
    @Override
    public String toString()
    {
        return Double.toString(innerSolution.fitness());
    }
}

package hmod.solvers.hh.adapters.mkp;

import hmod.domains.mkp.MKPSolution;
import hmod.solvers.hh.HHSolution;

/**
 *
 * @author Enrique Urra C.
 */
public class MKPHHSolution<T extends MKPHHSolution<T>> implements HHSolution<T>
{
    private final MKPSolution innerSolution;

    public MKPHHSolution(MKPSolution innerSolution)
    {
        this.innerSolution = innerSolution;
    }

    public final MKPSolution getInnerSolution()
    {
        return innerSolution;
    }
    
    @Override
    public double getEvaluation()
    {
        return innerSolution.getTotalProfit();
    }
    
    public double getPorcentualGap()
    {
        return innerSolution.getPorcentualGap();
    }

    @Override
    public String evaluationToString()
    {
        return innerSolution.getTotalProfit() + 
            (innerSolution.isGapAvailable() ? " (gap: " + innerSolution.getPorcentualGap() + ")" : "");
    }

    @Override
    public int compareTo(MKPHHSolution otherSolution)
    {
        if(getEvaluation() > otherSolution.getEvaluation())
            return 1;
        else if(getEvaluation() < otherSolution.getEvaluation())
            return -1;
        else
            return 0;
    }

    @Override
    public boolean isSameTo(T otherSolution)
    {
        return innerSolution.sameAs(otherSolution.getInnerSolution());
    }

    @Override
    public String toString()
    {
        return evaluationToString();
    }
}

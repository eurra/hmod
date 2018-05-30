
package hmod.solvers.hh.models.soltrack;

import hmod.solvers.hh.HHSolution;
import hmod.solvers.hh.HHSolutionHandler;
import java.io.PrintWriter;
import java.util.Objects;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 * @param <T>
 */
public final class TrackableSolutionHandler<T extends HHSolution> implements HHSolutionHandler<T>
{
    private T inputSolution;
    private T outputSolution;
    private T bestSoFar;
    private T worstSoFar;
    private boolean bestWasImproved;
    
    @Override
    public void setOutputSolution(T solution)
    {
        if(solution != null)
        {
            outputSolution = solution;
            
            if(bestSoFar == null || bestSoFar.compareTo(outputSolution) < 0)
            {
                bestSoFar = outputSolution;
                bestWasImproved = true;
            }
            else
            {
                bestWasImproved = false;
            }
            
            if(worstSoFar == null || worstSoFar.compareTo(outputSolution) > 0)
                worstSoFar = outputSolution;
        }
    }

    @Override
    public T getInputSolution()
    {
        return inputSolution;
    }

    @Override
    public T getOutputSolution()
    {
        return outputSolution;
    }
    
    public boolean outputImprovedInput()
    {
        Objects.requireNonNull(outputSolution, "Null output solution");        
        return inputSolution == null || outputSolution.compareTo(inputSolution) > 0;
    }

    public boolean outputImprovedBest()
    {
        return bestWasImproved;
    }
    
    public T getBestSolution()
    {
        return bestSoFar;
    }
    
    public T getWorstSolution()
    {
        return worstSoFar;
    }
    
    public void setInputSolution(T solution)
    {
        inputSolution = solution;
    }
    
    public void setInputFromOutput()
    {
        inputSolution = Objects.requireNonNull(outputSolution, "Null output solution");
    }
    
    public double getRelativeEvaluation(T solution)
    {
        if(solution.compareTo(worstSoFar) == 0)
            return 0;
        
        return Math.abs(solution.getEvaluation() - worstSoFar.getEvaluation());
    }
}

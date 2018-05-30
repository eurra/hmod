
package hmod.domains.mkp;

import java.io.PrintWriter;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
class MutableSolutionHandler implements SolutionHandler<MKPSolution>
{
    private MKPSolution providedSolution;
    private MKPSolution toRetrieveSolution;
    private MKPSolution bestSolution;

    public MutableSolutionHandler()
    {
    }
    
    public MKPSolution getProvidedSolution() throws IllegalStateException
    {
        if(providedSolution == null)
            throw new IllegalStateException("The provided solution has not been set");
        
        return providedSolution;
    }
    
    public void setSolutionForRetrieving(MKPSolution solution)
    {
        toRetrieveSolution = solution;
        
        if(solution != null)
        {
            OutputManager.println(MKPOutputIds.NEW_SOLUTION_INFO, "***********************\n\n" + toRetrieveSolution + "\n");

            if(bestSolution == null || bestSolution.getTotalProfit() < toRetrieveSolution.getTotalProfit())
            {
                bestSolution = toRetrieveSolution;
                OutputManager.println(MKPOutputIds.NEW_BEST_SOLUTION_INFO, "***********************\n\n" + bestSolution + "\n");
            }
            
            PrintWriter pw = OutputManager.getCurrent().getOutput(MKPOutputIds.WARNINGS);
            
            if(pw != null && providedSolution != null && providedSolution.sameAs(solution))
                pw.println("Warning: provided solution wasn't modified!");
        }
    }

    @Override
    public void provideSolution(MKPSolution solution)
    {
        providedSolution = solution;
    }

    @Override
    public MKPSolution retrieveSolution() throws IllegalStateException
    {
        if(toRetrieveSolution == null)
            throw new IllegalStateException("The solution to retrieve has not been set");
        
        return toRetrieveSolution;
    }
    
    @Override
    public MKPSolution getBestSolution() throws IllegalStateException
    {
        if(bestSolution == null)
            throw new IllegalStateException("No best solution has been set");
        
        return bestSolution;
    }

    @Override
    public boolean isSolutionProvided()
    {
        return providedSolution != null;
    }
}

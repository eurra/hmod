
package hmod.domains.binary;

public final class BinarySolutionHandler
{    
    private BinarySolution solution;
    private BinarySolution bestSolution;

    BinarySolutionHandler()
    {
    }

    public BinarySolution getSolution()
    {
        if(solution == null)
            throw new IllegalStateException("The solution has not been set");
        
        return solution;
    }
    
    public void setSolution(BinarySolution inputSolution)
    {
        if(inputSolution != null)
        {
            this.solution = inputSolution;

            if(bestSolution == null || bestSolution.fitness() < inputSolution.fitness())
                bestSolution = inputSolution;
        }
    }

    public BinarySolution getBestSolution()
    {
        return bestSolution;
    }
}

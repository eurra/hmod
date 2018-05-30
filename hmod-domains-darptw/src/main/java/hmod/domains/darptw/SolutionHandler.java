
package hmod.domains.darptw;

import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
public class SolutionHandler
{
    private final SolutionBuilder sb;
    private final FactorMap evalWeights;
    private final ProblemInstance problemInstance;
    private Evaluator evaluator;
    private DARPTWSolution inputSolution;
    private DARPTWSolution outputSolution;
    private DARPTWSolution bestSolution;
    private boolean weightsReported;

    SolutionHandler(ProblemInstance problemInstance, 
                    SolutionBuilder sb, 
                    FactorMap evalWeights)
    {
        this.problemInstance = problemInstance;
        this.sb = sb;
        this.evalWeights = evalWeights;
    }
    
    public void setInputSolution(DARPTWSolution solution)
    {
        inputSolution = solution;
    }
    
    public void loadEmptySolution() throws DARPTWException
    {
        Route[] routes = new Route[problemInstance.getVehiclesNumber()];
        DARPTWFactory factory = DARPTWFactory.getInstance();

        for(int i = 0; i < routes.length; i++)
            routes[i] = factory.createRoute(problemInstance);
        
        sb.importRoutes(routes);
    }

    public void loadInputSolution()
    {
        sb.importRoutes(Objects.requireNonNull(inputSolution, "null input solution").getRoutes());
    }
    
    public DARPTWSolution saveSolutionToOutput() throws DARPTWException
    {
        if(evaluator == null)
            this.evaluator = DARPTWFactory.getInstance().createEvaluator(problemInstance);

        evaluator.reset();
        List<Route> routes = sb.getRoutesList();

        for(Route route : routes)
            evaluator.addRoute(route);

        outputSolution = evaluator.getSolution(evalWeights, true);

        if(!weightsReported)
        {
            PrintWriter pw = OutputManager.getCurrent().getOutput(DARPTWOutputIds.EVAL_INFO);

            if(pw != null)
            {
                pw.println("**** Evaluation parameters information ****\n");
                pw.println("1) Operator system weights:\n");
                pw.println("Transit time: " + evalWeights.getFactorValue(Factor.TRANSIT_TIME).getValue());
                pw.println("Route duration: " + evalWeights.getFactorValue(Factor.ROUTE_DURATION).getValue());
                pw.println("Slack time: " + evalWeights.getFactorValue(Factor.SLACK_TIME).getValue());                
                pw.println("\n2) Service quality weights:\n");
                pw.println("Ride time: " + evalWeights.getFactorValue(Factor.RIDE_TIME).getValue());
                pw.println("Excess ride time: " + evalWeights.getFactorValue(Factor.EXCESS_RIDE_TIME).getValue());
                pw.println("Wait time: " + evalWeights.getFactorValue(Factor.WAIT_TIME).getValue());                
                pw.println("\n3) Unfeasibility weights:\n");
                pw.println("Time windows violation: " + evalWeights.getFactorValue(Factor.TIME_WINDOWS_VIOLATION).getValue());
                pw.println("Max. route duration violation: " + evalWeights.getFactorValue(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION).getValue());
                pw.println("Max. ride time violation: " + evalWeights.getFactorValue(Factor.MAXIMUM_RIDE_TIME_VIOLATION).getValue());
            }

            weightsReported = true;
        }

        if(bestSolution == null || bestSolution.getFinalScore() > outputSolution.getFinalScore())
        {
            bestSolution = outputSolution;            
            PrintWriter pw = OutputManager.getCurrent().getOutput(DARPTWOutputIds.BEST_DETAIL_INFO);

            if(pw != null)
                pw.println("***********************\n\n" + outputSolution + "\n");
        }
        
        return outputSolution;
    }

    public DARPTWSolution getBestSolution()
    {
        return Objects.requireNonNull(bestSolution, "No best solution has been registered");
    }
    
    public DARPTWSolution getInputSolution()
    {
        return Objects.requireNonNull(inputSolution, "No input solution was provided");
    }

    public DARPTWSolution getOutputSolution()
    {
        return Objects.requireNonNull(outputSolution, "No output solution was provided");
    }

    public boolean isInputSolutionProvided()
    {
        return inputSolution != null;
    }
}

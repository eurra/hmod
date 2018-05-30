
package hmod.domains.darptw;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.BitSet;

/**
 *
 * @author Enrique Urra C.
 */
public class DARPTWSolution
{
    private final FactorMap evaluation;
    private final RouteInfo[] routeInfos;
    private final BitSet clientsMask;
    private final double finalScore;

    DARPTWSolution(
            FactorMap evaluation,
            RouteInfo[] routesInfos,
            BitSet clientsMask,
            FactorMap evalWeights)
    {
        if(evalWeights.getFactorsCount() == 0)
            throw new IllegalArgumentException("The weights map is empty");
        
        double calcScore = 0.0;
        Factor[] weightedFactors = evalWeights.getFactors();
        
        for(Factor factor : weightedFactors)
            calcScore += evaluation.getFactorValue(factor).getValue() * evalWeights.getFactorValue(factor).getValue();
        
        this.evaluation = evaluation;
        this.finalScore = calcScore;
        this.routeInfos = routesInfos;
        this.clientsMask = clientsMask;
    }

    public FactorMap getEvaluation()
    {
        return evaluation;
    }
    
    public double getFinalScore()
    {
        return finalScore;
    }

    public RouteInfo getRouteInfo(int routeIndex)
    {
        if(routeIndex <= 0 || routeIndex > routeInfos.length)
            throw new IllegalArgumentException("The provided index is not valid");
        
        return routeInfos[--routeIndex];
    }

    public Route[] getRoutes()
    {
        int maxVehicles = routeInfos.length;
        Route[] res = new Route[maxVehicles];
        
        for(int i = 0; i < maxVehicles; i++)
            res[i] = routeInfos[i].getRoute().clone();
        
        return res;
    }

    public int getRoutesCount()
    {
        return routeInfos.length;
    }
    
    public boolean isFeasible()
    {
        return remainingClientsCount() > 0 && 
            evaluation.getFactorValue(Factor.TIME_WINDOWS_VIOLATION).getValue() + 
            evaluation.getFactorValue(Factor.MAXIMUM_ROUTE_DURATION_VIOLATION).getValue() +
            evaluation.getFactorValue(Factor.MAXIMUM_RIDE_TIME_VIOLATION).getValue() <= 0.0;
    }

    public int remainingClientsCount()
    {
        return clientsMask.size() - clientsMask.cardinality();
    }

    @Override
    public String toString()
    {
        int routesCount = routeInfos.length;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.#", otherSymbols);
        Factor[] usedFactors = evaluation.getFactors();
        
        String ret = "Solution score: " + finalScore + (!isFeasible() ? " (Unfeasible)" : "");
        
        for(int i = 0; i < usedFactors.length; i++)
            ret += "\nTotal " + usedFactors[i].getName() + ": " + df.format(evaluation.getFactorValue(usedFactors[i]).getValue());
        
        ret += "\n\n";
                
        for(int i = 0; i < routesCount; i++)
        {
            RouteInfo routeInfo = routeInfos[i];
            FactorMap routeEvaluation = routeInfo.getEvaluation();
            int eventsCount = routeInfo.getEventsCount();
            
            ret += "*** Route " + (i + 1) + " *** (clients: " + ((eventsCount - 2) / 2);
            
            for(int j = 0; j < usedFactors.length; j++)
                ret += "; " + usedFactors[j].getName() + ": " + df.format(routeEvaluation.getFactorValue(usedFactors[j]).getValue());
            
            ret += ")\n" +routeInfo.getRoute() + "\n";
            
            for(int j = 0; j < eventsCount; j++)
            {
                EventInfo evInfo = routeInfo.getEventInfo(j);
                ret += evInfo + (j == eventsCount - 1 ? "\n" : "   --->   ");
            }
            
            if(i < routesCount - 1)
                ret += "\n";
        }
        
        return ret;
    }
}

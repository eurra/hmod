
package hmod.solvers.hh.models.oscillation;

import hmod.solvers.hh.HHSolution;
import hmod.solvers.hh.models.soltrack.TrackableSolutionHandler;
import hmod.solvers.hh.models.tabulist.TabuList;
import optefx.util.output.OutputManager;

/**
 * @author Enrique Urra C.
 */
public final class OscillationHandler
{    
    private final TabuList tl;
    private final TrackableSolutionHandler tsh;
    private final double oscillationModifier;
    private final double growProportion;
    private double piMultiplier = Math.PI / 2;
    private double oscillationRate;
    private boolean oscillationEnabled = true;

    OscillationHandler(TabuList tl,
                       TrackableSolutionHandler tsh,
                       double oscillationModifier, 
                       double growProportion)
    {
        this.tl = tl;
        this.tsh = tsh;
        this.growProportion = growProportion;
        this.oscillationModifier = oscillationModifier;
    }
    
    public double getCurrentOscillationRate()
    {
        return oscillationRate /*+ (1 - oscillationRate) * (1 - Math.tanh(sth.getLocalNoImproveIterations() / 20.0))*/;
    }

    public double getGrowProportion()
    {
        return growProportion;
    }

    public double getPIMultiplier()
    {
        return piMultiplier;
    }

    public boolean isOscillationEnabled()
    {
        return oscillationEnabled;
    }
    
    public void enableOscillation()
    {
        this.oscillationEnabled = true;
        restartPiMultiplier();
    }
    
    public void disableOscillation()
    {
        OutputManager.println(StrategyOscillationOutputIds.EVENTS, "Oscillation complete, returning to base strategy...");
        this.oscillationEnabled = false;
    }
    
    /**
     * Updates the current oscillation rate, according to the following elliptic 
     * cosine function cose(p):
     *   om = oscillation modifier
     *   p = pi multiplier
     * 
     *   a(p) = 1 + (om * tan(p + 0.5 * pi)) ^ 2
     *   cose(p) = (1 + ((+/-)1 / sqrt(a(p)))) / 2
     */
    public void updateOscillationRate()
    {
        double realMultiplier = piMultiplier;
        
        if(realMultiplier > 3 * (Math.PI / 2))
            realMultiplier = 3 * (Math.PI / 2);
        
        double sign = realMultiplier < Math.PI ? 1.0 : -1.0;
        double innerValue = 1 + Math.pow(oscillationModifier * Math.tan(realMultiplier + 0.5 * Math.PI), 2.0);        
        oscillationRate = (1 + (sign / Math.sqrt(innerValue))) / 2;
    }
    
    private double bounceFactor = 1.0;
    
    /**
     * Updates the current PI multiplier according to the following logarithmic 
     * function, which is based on the strategy change speed (u) and the current 
     * global non-improving iteration count (t):
     * 
     *   p = (pi/2) + ln(1 + u*t)
     */
    public void updatePIMultiplier()
    {
        //*
        HHSolution outputSol = tsh.getOutputSolution();
        HHSolution inputSol = tsh.getInputSolution();
        HHSolution bestSol = tsh.getBestSolution();
        boolean outputIsTabued = tl.contains(outputSol);
        
        if(inputSol == null || (!outputIsTabued && bestSol.compareTo(outputSol) == 0))
        {
            piMultiplier = Math.PI / 2;
            bounceFactor = 1.0;
        }
        else
        {
            double ratio, mult;
            double bestRelativeEval = tsh.getRelativeEvaluation(bestSol);

            if(bestRelativeEval == 0)
                ratio = 1;
            else
                ratio = tsh.getRelativeEvaluation(outputSol) / bestRelativeEval;

            if(!outputIsTabued && inputSol.compareTo(outputSol) < 0)
                mult = -1;
            else
                mult = 1;
            
            if(bounceFactor > 1.0)
                mult *= bounceFactor;
        
            double toAdd = mult * ratio * growProportion;
            piMultiplier += toAdd;
        }
        
        if(piMultiplier < Math.PI / 2)
        {
            piMultiplier = Math.PI / 2;
            bounceFactor = 1.0;
        }
        else if(piMultiplier > 3 * Math.PI / 2)
        {
            piMultiplier = 3 * Math.PI / 2;
            bounceFactor++;
        }
        else if(bounceFactor > 1.0)
        {
            bounceFactor = 1.0;
        }
        
        /*if(inputSol == null || bestSol.compareTo(outputSol) == 0)
        {
            piMultiplier = Math.PI / 2;
        }
        else
        {
        if(inputSol != null)
        {
            double ratio, multip;
            
            if(inputSol.compareTo(outputSol) < 0)
            {
                ratio = tsh.getRelativeEvaluation(inputSol) / tsh.getRelativeEvaluation(outputSol);
                multip = -1;
            }
            else
            {
                ratio = tsh.getRelativeEvaluation(outputSol) / tsh.getRelativeEvaluation(inputSol);
                multip = 1;
            }
            
            piMultiplier = (Math.PI / 2) + multip * ratio * growProportion;
        }
        
        if(piMultiplier < Math.PI / 2)
            piMultiplier = Math.PI / 2;
        else if(piMultiplier > 3 * Math.PI / 2)
            piMultiplier = 3 * Math.PI / 2;
        */  
        
        /*
        //piMultiplier = (Math.PI / 2) + Math.log10(1 + growProportion * sth.getGlobalNoImproveIterations());
        piMultiplier = (Math.PI / 2) * 3.0 * Math.tanh(growProportion * sth.getGlobalNoImproveIterations());
        
        if(piMultiplier == 3 * (Math.PI / 2))
        {
            piMultiplier += 1;
            OutputManager.println(StrategyOscillationOutputIds.EVENTS, "Reached max. diversification");
        }
        */
    }
    
    /**
     * Checks if the current pi multiplier remains within its valid bounds.
     * @return true if it remains within valid bounds
     */
    public boolean checkPIMultiplier()
    {
        return piMultiplier <= (1.5 * Math.PI);
    }
    
    /**
     * Force a restart on the current strategy.
     */
    public void restartPiMultiplier()
    {
        OutputManager.println(StrategyOscillationOutputIds.EVENTS, "Switching to intensification...");
        piMultiplier = 0.5 * Math.PI;
    }
}

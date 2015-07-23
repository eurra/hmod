
package hmod.solvers.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public final class RouletteSelector
{
    private final List<Double> entries;
    private double calculatedSum = -1.0;
    private double amplificator = 0;

    public RouletteSelector(List<Double> entries)
    {
        this.entries = Objects.requireNonNull(entries, "null entries");
    }
    
    public RouletteSelector setCalculatedSum(double sum) throws IllegalArgumentException
    {
        if(sum <= 0.0) throw new IllegalArgumentException("Illegal sum: " + sum);
        calculatedSum = sum;
        
        return this;
    }
    
    public RouletteSelector setAmplificator(double ampl) throws IllegalArgumentException
    {
        if(ampl < 0) throw new IllegalArgumentException("Illegal amplificator: " + ampl);
        amplificator = ampl;
        
        return this;
    }
    
    public int select()
    {
        List<Double> entriesToUse = entries;
        boolean sumCalculated = calculatedSum != -1.0;
        boolean requiresAmplification = amplificator > 0;
        
        if(!sumCalculated)
            calculatedSum = 0.0;
        
        int entriesCount = entriesToUse.size();
        List<Double> finalEntries;
        
        if(!sumCalculated || requiresAmplification)
        {
            AtomicReference<Integer> multiplier = new AtomicReference<>(entriesCount);
            finalEntries = new ArrayList<>(entriesCount);
            
            for(int i = 0; i < entriesCount; i++)
            {
                double val = entriesToUse.get(i);
                double finalValue;
                
                if(requiresAmplification)
                {
                    double currMultiplier = multiplier.getAndUpdate(n -> n - 1);
                    finalValue = val * amplificator * currMultiplier * (1 - Math.sqrt((entriesCount - currMultiplier) / entriesCount)); 
                }
                else
                {
                    finalValue = val;
                }

                if(!sumCalculated)
                    calculatedSum += finalValue;

                finalEntries.add(finalValue);
            }
        }
        else
        {
            finalEntries = entriesToUse;
        }
        
        double prob = RandomTool.getDouble();
        double acumProb = 0.0;

        for(int i = 0; i < entriesCount; i++)
        {
            double rangeDiff = finalEntries.get(i) / calculatedSum;
            double upperRange = acumProb + rangeDiff;

            if(prob >= acumProb && prob <= upperRange)
                return i;
            else
                acumProb += rangeDiff;
        }
        
        return entriesCount - 1;
    }
}

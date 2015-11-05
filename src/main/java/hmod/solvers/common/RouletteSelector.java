
package hmod.solvers.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import optefx.util.random.RandomTool;

/**
 *
 * @author Enrique Urra C.
 */
public final class RouletteSelector<T> implements Selector<T>
{
    public static RouletteSelector<Double> defaultRoulette()
    {
        return new RouletteSelector<>((v) -> v);
    }
    
    private final List<T> orderedElements;
    private final Function<T, Double> converter;
    private double amplificator = 1.0;
    private double currentSum = 0.0;
    
    public RouletteSelector(Function<T, Double> converter)
    {
        this(10, converter);
    }
    
    public RouletteSelector(int size, Function<T, Double> converter)
    {
        this.converter = Objects.requireNonNull(converter, "null converter");
        this.orderedElements = new ArrayList<>(size);
    }
    
    @Override
    public RouletteSelector<T> addElement(T elem)
    {
        orderedElements.add(elem);
        currentSum += converter.apply(elem);
        
        return this;
    }
    
    @Override
    public RouletteSelector<T> addAll(Collection<T> elements)
    {
        for(T elem : elements)
            addElement(elem);
        
        return this;
    }
    
    @Override
    public RouletteSelector<T> removeElement(T elem)
    {
        orderedElements.remove(elem);
        currentSum -= converter.apply(elem);
        
        return this;
    }
    
    public RouletteSelector<T> sortElements(Comparator<? super T> comp)
    {
        orderedElements.sort(comp);
        return this;
    }
    
    @Override
    public RouletteSelector<T> clear()
    {
        orderedElements.clear();
        currentSum = 0.0;
        
        return this;
    }
    
    @Override
    public boolean isEmpty()
    {
        return orderedElements.isEmpty();
    }
    
    @Override
    public int getElementsCount()
    {
        return orderedElements.size();
    }
    
    public RouletteSelector<T> setAmplificator(double ampl) throws IllegalArgumentException
    {
        if(ampl < 1.0) throw new IllegalArgumentException("Illegal amplificator: " + ampl);
        amplificator = ampl;
        
        return this;
    }
    
    @Override
    public T select()
    {
        List<Double> valuesToUse = new ArrayList<>(orderedElements.size());
        double sumToUse = 0.0;
        boolean requiresAmplification = amplificator > 1.0;
        
        for(T elem : orderedElements)
        {
            double elemValue = converter.apply(elem);
            
            if(requiresAmplification)
            {
                double elemProp = elemValue / currentSum;
                double amplifiedValue = elemValue * (1 + elemProp * amplificator);
                valuesToUse.add(amplifiedValue);
                sumToUse += amplifiedValue;
            }
            else
            {
                valuesToUse.add(elemValue);
                sumToUse += elemValue;
            }
        }
        
        int entriesCount = valuesToUse.size();
        double prob = RandomTool.getDouble();
        double acumProb = 0.0;

        for(int i = 0; i < entriesCount; i++)
        {
            double rangeDiff = valuesToUse.get(i) / sumToUse;
            double upperRange = acumProb + rangeDiff;

            if(prob >= acumProb && prob <= upperRange)
                return orderedElements.get(i);
            else
                acumProb += rangeDiff;
        }
        
        return orderedElements.get(entriesCount - 1);
    }
}

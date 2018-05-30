
package hmod.domains.darptw;

import java.util.LinkedHashMap;

/**
 *
 * @author Enrique Urra C.
 */
public class MutableFactorMap implements FactorMap
{
    private final LinkedHashMap<Factor, FactorValue> factorMap;

    public MutableFactorMap()
    {
        this.factorMap = new LinkedHashMap<>();
    }

    private MutableFactorMap(LinkedHashMap<Factor, FactorValue> factorMap)
    {
        this.factorMap = factorMap;
    }
    
    public void addFactor(Factor factor, FactorValue value)
    {
        if(factor == null)
            throw new NullPointerException("Null factor");
        
        if(value == null)
            throw new NullPointerException("Null factor value");
        
        factorMap.put(factor, value);
    }

    @Override
    public FactorValue getFactorValue(Factor factor)
    {
        if(!factorMap.containsKey(factor))
            return FactorValue.create(0.0);
        
        return factorMap.get(factor);
    }

    @Override
    public Factor[] getFactors()
    {
        return factorMap.keySet().toArray(new Factor[0]);
    }

    @Override
    public boolean existsFactor(Factor factor)
    {
        return factorMap.containsKey(factor);
    }

    @Override
    public int getFactorsCount()
    {
        return factorMap.size();
    }

    @Override
    public FactorMap clone()
    {
        LinkedHashMap<Factor, FactorValue> mapCopy = new LinkedHashMap<>(factorMap.size());
        
        for(Factor factor : factorMap.keySet())
            mapCopy.put(factor, factorMap.get(factor));
        
        return new MutableFactorMap(mapCopy);
    }
}


package hmod.domains.mkp;

import java.io.IOException;

/**
 *
 * @author Enrique Urra C.
 */
public final class ProblemInstanceHandler implements MKPProblemInstance
{
    private final MKPProblemInstance instance;
    private double lpOptimum = -1.0;
    
    ProblemInstanceHandler(String file, int instanceNumber, String lpOptimumsFile) throws IndexOutOfBoundsException
    {
        MKPParser parser = new MKPParser();
        
        try
        {
            MKPProblemInstance[] instances = parser.parse(file);
            instance = instances[instanceNumber];
            
            if(lpOptimumsFile != null)
            {
                lpOptimum = parser.findLPOptimum(
                    lpOptimumsFile,
                    instance.getResourcesCount(), 
                    instance.getItemsCount(), 
                    instanceNumber
                );
            }
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Cannot initialize the problem instance in file '" + file + "'", ex);
        }
    }

    @Override
    public int getNumber()
    {
        return instance.getNumber();
    }
    
    @Override
    public int getItemsCount()
    {
        return instance.getItemsCount();
    }

    @Override
    public int getResourcesCount()
    {
        return instance.getResourcesCount();
    }

    @Override
    public Item getItem(int itemId)
    {
        return instance.getItem(itemId);
    }

    @Override
    public Resource getResource(int resourceId)
    {
        return instance.getResource(resourceId);
    }

    @Override
    public boolean itemExists(Item item)
    {
        return instance.itemExists(item);
    }

    @Override
    public boolean resourceExists(Resource res)
    {
        return instance.resourceExists(res);
    }

    @Override
    public void checkItem(Item item) throws IllegalArgumentException
    {
        instance.checkItem(item);
    }

    @Override
    public void checkResource(Resource res) throws IllegalArgumentException
    {
        instance.checkResource(res);
    }

    @Override
    public int getWeight(Item item, Resource resource)
    {
        return instance.getWeight(item, resource);
    }

    @Override
    public double getLPOptimum()
    {
        return lpOptimum;
    }

    @Override
    public boolean isLPOptimumAvailable()
    {
        return lpOptimum != -1.0;
    }
}


package optefx.util.metadata;

import java.util.List;
import java.util.WeakHashMap;

/**
 *
 * @author Enrique Urra C.
 */
class WeakMapManager extends MetadataManager
{
    private final WeakHashMap<Object, StandaloneProvider> cachedData;

    public WeakMapManager()
    {
        cachedData = new WeakHashMap<>();
    }

    @Override
    public <T> T attachData(T target, Metadata metadata)
    {
        StandaloneProvider currData = cachedData.get(target);
        
        if(currData == null)
        {
            currData = new StandaloneProvider();
            cachedData.put(target, currData);
        }
        
        currData.addData(metadata);
        return target;
    }

    @Override
    public <T extends Metadata> T getDataFor(Object target, Class<T> dataType)
    {
        StandaloneProvider currData = cachedData.get(target);
        
        if(currData == null)
            return null;
        
        return (T)currData.getData(dataType);
    }

    @Override
    public <T extends Metadata> List<T> getAllDataFor(Object target, Class<T> dataType)
    {
        StandaloneProvider currData = cachedData.get(target);
        
        if(currData == null)
            return null;
        
        return currData.getAllData(dataType);
    }
    
    @Override
    public boolean hasData(Object target, Class<? extends Metadata> dataType)
    {
        return getDataFor(target, dataType) != null;
    }
}

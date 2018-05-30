
package optefx.util.metadata;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
public final class StandaloneProvider<M extends Metadata> implements MutableMetadataProvider<M>
{
    private final ArrayList<M> data;

    public StandaloneProvider()
    {
        this.data = new ArrayList<>();
    }

    @Override
    public StandaloneProvider addData(M... data)
    {
        for(int i = 0; i < data.length; i++)
        {
            if(data[i] == null)
                throw new NullPointerException("Null data at position " + i);
            
            this.data.add(data[i]);
        }
        
        return this;
    }

    @Override
    public <T extends M> T getData(Class<T> dataType)
    {
        if(dataType == null)
            throw new NullPointerException("Null data type");
        
        for(Metadata singleData : data)
        {
            if(dataType.isAssignableFrom(singleData.getClass()))
                return (T)singleData;
        }
        
        return null;
    }

    @Override
    public <T extends M> List<T> getAllData(Class<T> dataType)
    {
        if(dataType == null)
            throw new NullPointerException("Null data type");
        
        ArrayList<T> specificData = new ArrayList<>();
        
        for(M singleData : data)
        {
            if(dataType.isAssignableFrom(singleData.getClass()))
                specificData.add((T)singleData);
        }
        
        return specificData;
    }

    @Override
    public boolean hasData(Class<? extends M> dataType)
    {
        return getData(dataType) != null;
    }
}


package optefx.util.metadata;

import java.util.List;

/**
 *
 * @author Enrique Urra C.
 */
public final class ProxyProvider<M extends Metadata> implements MutableMetadataProvider<M>
{
    private final Object owner;

    public ProxyProvider(Object owner)
    {
        if(owner == null)
            throw new NullPointerException("Null owner");
        
        this.owner = owner;
    }

    @Override
    public ProxyProvider addData(M... data)
    {
        MetadataManager manager = MetadataManager.getInstance();
        
        for(int i = 0; i < data.length; i++)
        {
            if(data[i] == null)
                throw new NullPointerException("Null data");
            
            manager.attachData(owner, data[i]);
        }
        
        return this;
    }
    
    @Override
    public <T extends M> T getData(Class<T> dataType)
    {
        return MetadataManager.getInstance().getDataFor(owner, dataType);
    }

    @Override
    public <T extends M> List<T> getAllData(Class<T> dataType)
    {
        return MetadataManager.getInstance().getAllDataFor(owner, dataType);
    }

    @Override
    public boolean hasData(Class<? extends M> dataType)
    {
        return MetadataManager.getInstance().hasData(owner, dataType);
    }
}


package hmod.launcher;

import java.util.HashMap;
import optefx.util.metadata.MetadataManager;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultAlgorithmLoaderHandler implements AlgorithmLoaderHandler
{
    private final HashMap<String, AlgorithmLoader> loaders;
    private final HashMap<String, AlgorithmLoaderInfo> infos;
    private String current;

    public DefaultAlgorithmLoaderHandler(AlgorithmLoader[] loaders)
    {
        if(loaders == null)
            throw new NullPointerException("Null loader array");
        
        MetadataManager metadataManager = MetadataManager.getInstance();
        this.loaders = new HashMap<>(loaders.length);
        this.infos = new HashMap<>(loaders.length);
        
        for(int i = 0; i < loaders.length; i++)
        {
            AlgorithmLoader currLoader = loaders[i];
            
            if(currLoader == null)
                throw new NullPointerException("Null loader at position " + i);
            
            AlgorithmLoaderInfo currLoaderInfo = metadataManager.getDataFor(currLoader, AlgorithmLoaderInfo.class);
            
            if(currLoaderInfo == null)
                throw new NullPointerException("The loader at position " + i + "has not been tagged with metadata");
            
            String currId = currLoaderInfo.getId();
            this.loaders.put(currId, currLoader);
            this.infos.put(currId, currLoaderInfo);
            
            if(current == null)
                current = currId;
        }
        
        if(current == null)
            throw new IllegalArgumentException("No valid loaders were provided");
    }

    @Override
    public void enableLoader(String id) throws IllegalArgumentException
    {
        if(!loaders.containsKey(id))
            throw new IllegalArgumentException("The loader id '" + id + "' has not been registered");
        
        current = id;
    }

    @Override
    public AlgorithmLoader getCurrentLoader()
    {
        return loaders.get(current);
    }

    @Override
    public AlgorithmLoaderInfo getInfoFor(String id) throws IllegalArgumentException
    {
        if(!loaders.containsKey(id))
            throw new IllegalArgumentException("The loader id '" + id + "' has not been registered");
        
        return infos.get(id);
    }

    @Override
    public String[] getSupportedLoaders()
    {
        return loaders.keySet().toArray(new String[0]);
    }
}

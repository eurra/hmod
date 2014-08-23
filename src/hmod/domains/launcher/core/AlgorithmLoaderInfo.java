
package hmod.domains.launcher.core;

import flexbuilders.core.BuildException;
import flexbuilders.core.Builder;
import optefx.util.metadata.Metadata;

/**
 *
 * @author Enrique Urra C.
 */
public final class AlgorithmLoaderInfo implements Metadata
{
    public static final class AlgorithmLoaderInfoBuilder implements Builder<AlgorithmLoaderInfo>
    {
        private AlgorithmLoaderInfo info;

        public AlgorithmLoaderInfoBuilder()
        {
            this.info = new AlgorithmLoaderInfo();
        }
        
        public AlgorithmLoaderInfoBuilder setId(String id)
        {
            info.id = id;
            return this;
        }
        
        public AlgorithmLoaderInfoBuilder setName(String name)
        {
            info.name = name;
            return this;
        }
        
        public AlgorithmLoaderInfoBuilder setDescription(String desc)
        {
            info.description = desc;
            return this;
        }

        @Override
        public AlgorithmLoaderInfo build() throws BuildException
        {
            if(info.id == null)
                throw new BuildException("The id has not been set");
            
            AlgorithmLoaderInfo toRet = info;
            info = new AlgorithmLoaderInfo();
            
            return toRet;
        }
    }
    
    public static AlgorithmLoaderInfoBuilder builder()
    {
        return new AlgorithmLoaderInfoBuilder();
    }
    
    private String id;
    private String name;
    private String description;

    private AlgorithmLoaderInfo()
    {
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        if(name == null)
            return "non-set";
        
        return name;
    }

    public String getDescription()
    {
        if(description == null)
            return "non-set";
        
        return description;
    }
}

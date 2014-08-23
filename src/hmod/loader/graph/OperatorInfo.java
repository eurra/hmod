
package hmod.loader.graph;

import optefx.util.metadata.Metadata;
import flexbuilders.core.Builder;

/**
 *
 * @author Enrique Urra C.
 */
public class OperatorInfo implements Metadata
{
    public static class OperatorInfoBuilder implements Builder<OperatorInfo>
    {
        private Enum category;
        private String name;
        private String description;
        
        public OperatorInfoBuilder()
        {
        }
        
        public OperatorInfoBuilder category(Enum category)
        {
            this.category = category;
            return this;
        }

        public OperatorInfoBuilder name(String name)
        {
            this.name = name;
            return this;
        }

        public OperatorInfoBuilder description(String description)
        {
            this.description = description;
            return this;
        }

        @Override
        public OperatorInfo build()
        {
            return new OperatorInfo(category, name, description);
        }
    }
    
    private final Enum category;
    private final String name;
    private final String description;

    public OperatorInfo(Enum category, String name, String description)
    {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    public Enum getCategory()
    {
        return category;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }
}

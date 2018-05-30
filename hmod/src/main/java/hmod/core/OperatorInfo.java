
package hmod.core;

import optefx.util.metadata.Metadata;

/**
 *
 * @author Enrique Urra C.
 */
public final class OperatorInfo implements Metadata
{
    public static final class OperatorInfoBuilder
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

        public OperatorInfo build()
        {
            return new OperatorInfo(category, name, description);
        }
    }
    
    private final Enum category;
    private final String name;
    private final String description;

    private OperatorInfo(Enum category, String name, String description)
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

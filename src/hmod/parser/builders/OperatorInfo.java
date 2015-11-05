
package hmod.parser.builders;

import optefx.util.metadata.Metadata;
import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;

/**
 *
 * @author Enrique Urra C.
 */
public class OperatorInfo implements Metadata
{
    public static class Builder implements Buildable<OperatorInfo>
    {
        private Enum category;
        private String name;
        private String description;
        
        public Builder()
        {
        }
        
        public Builder category(Enum category)
        {
            this.category = category;
            return this;
        }

        public Builder name(String name)
        {
            this.name = name;
            return this;
        }

        public Builder description(String description)
        {
            this.description = description;
            return this;
        }

        @Override
        public OperatorInfo build() throws BuildException
        {
            return new OperatorInfo(category, name, description);
        }
    }
    
    private Enum category;
    private String name;
    private String description;

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

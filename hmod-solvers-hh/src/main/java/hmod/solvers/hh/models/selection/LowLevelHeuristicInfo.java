
package hmod.solvers.hh.models.selection;

import hmod.core.Statement;
import java.util.Objects;
import optefx.util.metadata.Metadata;
import optefx.util.metadata.MetadataManager;

/**
 *
 * @author Enrique Urra C.
 */
public final class LowLevelHeuristicInfo implements Metadata
{
    public static <T extends Statement> T tagLowLevelHeuristic(T heuristic, LowLevelHeuristicInfo info)
    {
        Objects.requireNonNull(heuristic, "null heuristic");
        Objects.requireNonNull(info, "null info");
        
        MetadataManager.getInstance().attachData(heuristic, info);
        return heuristic;
    }
    
    public static final class Builder
    {
        private String id;
        private String name;
        private String description;
        
        public Builder()
        {
        }
        
        public Builder id(String id)
        {
            this.id = id;
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

        public LowLevelHeuristicInfo build()
        {
            return new LowLevelHeuristicInfo(id, name, description);
        }
    }
    
    private final String id;
    private final String name;
    private final String description;

    private LowLevelHeuristicInfo(String id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId()
    {
        return id;
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

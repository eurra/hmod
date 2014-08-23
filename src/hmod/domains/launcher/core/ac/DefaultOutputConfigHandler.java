
package hmod.domains.launcher.core.ac;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import optefx.util.output.OutputConfigBuilder;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultOutputConfigHandler implements OutputConfigHandler
{
    private class FileOutputEntry
    {
        private final String path;
        private final boolean append;

        public FileOutputEntry(String path, boolean append)
        {
            this.path = path;
            this.append = append;
        }

        public String getPath()
        {
            return path;
        }

        public boolean isAppend()
        {
            return append;
        }
    }
    
    private final Map<String, FileOutputEntry> fileOutputs;
    private final Set<String> systemOutputs;

    public DefaultOutputConfigHandler()
    {
        this.fileOutputs = new HashMap<>();
        this.systemOutputs = new HashSet<>();
        
        restartConfig();
    }
    
    @Override
    public void addFileOutput(String id, String path, boolean append)
    {
        if(id == null || id.isEmpty())
            throw new IllegalArgumentException("null or empty id");
        
        if(path == null || path.isEmpty())
            throw new IllegalArgumentException("null or empty path");
        
        fileOutputs.put(id, new FileOutputEntry(path, append));
    }

    @Override
    public void addSystemOutput(String id)
    {
        if(id == null || id.isEmpty())
            throw new IllegalArgumentException("null or empty id");
        
        systemOutputs.add(id);
    }

    @Override
    public final void restartConfig()
    {
        fileOutputs.clear();
        systemOutputs.clear();
    }

    @Override
    public OutputConfigBuilder createConfigBuilder()
    {
        OutputConfigBuilder builder = new OutputConfigBuilder();
        
        for(String fileId : fileOutputs.keySet())
        {
            FileOutputEntry entry = fileOutputs.get(fileId);
            builder.addFileOutput(fileId, entry.getPath(), entry.isAppend());
        }
        
        for(String systemId : systemOutputs)
            builder.addSystemOutputId(systemId);
        
        return builder;
    }
    
}

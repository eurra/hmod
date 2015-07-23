
package hmod.launcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import optefx.util.output.OutputConfig;

/**
 *
 * @author Enrique Urra C.
 */
public final class OutputConfigHandler
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

    OutputConfigHandler()
    {
        this.fileOutputs = new HashMap<>();
        this.systemOutputs = new HashSet<>();
        
        restartConfig();
    }
    
    public void addFileOutput(String id, String path, boolean append)
    {
        if(id == null || id.isEmpty())
            throw new IllegalArgumentException("null or empty id");
        
        if(path == null || path.isEmpty())
            throw new IllegalArgumentException("null or empty path");
        
        fileOutputs.put(id, new FileOutputEntry(path, append));
    }

    public void addSystemOutput(String id)
    {
        if(id == null || id.isEmpty())
            throw new IllegalArgumentException("null or empty id");
        
        systemOutputs.add(id);
    }

    public final void restartConfig()
    {
        fileOutputs.clear();
        systemOutputs.clear();
    }

    public OutputConfig createConfig()
    {
        OutputConfig config = new OutputConfig();
        
        for(String fileId : fileOutputs.keySet())
        {
            FileOutputEntry entry = fileOutputs.get(fileId);
            config.addFileOutput(fileId, entry.getPath(), entry.isAppend());
        }
        
        for(String systemId : systemOutputs)
            config.addSystemOutputId(systemId);
        
        return config;
    }
}

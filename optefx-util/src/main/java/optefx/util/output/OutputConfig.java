
package optefx.util.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * The default configuration builder implementation.
 * @author Enrique Urra C.
 */
public final class OutputConfig
{
    private final class OutputConfigEntry
    {
        private final List<WriterBuilder> builders;
        private boolean permanent;

        public OutputConfigEntry()
        {
            this.builders = new ArrayList<>();
        }

        public OutputConfigEntry enableSystemOutput()
        {
            addBuilder(() -> systemOutput);
            return this;
        }

        public OutputConfigEntry enableSystemErrorOutput()
        {
            addBuilder(() -> systemErrorOutput);
            return this;
        }
        
        public OutputConfigEntry addFileOutput(String filePath)
        {
            return addFileOutput(() -> filePath);
        }

        public OutputConfigEntry addFileOutput(String filePath, boolean append)
        {
            return addFileOutput(() -> filePath, append);
        }
        
        public OutputConfigEntry addFileOutput(Supplier<String> filePathSupplier)
        {
            return addFileOutput(filePathSupplier, true);
        }
        
        public OutputConfigEntry addFileOutput(Supplier<String> filePathSupplier, boolean append)
        {
            addBuilder(new FileWriterBuilder(filePathSupplier).enableAppend(append));
            return this;
        }

        public void makePersistent()
        {
            this.permanent = true;
        }

        public boolean isPermanent()
        {
            return permanent;
        }

        private void addBuilder(WriterBuilder builder)
        {
            builders.add(builder);
        }

        public WriterBuilder[] getBuilders()
        {
            return builders.toArray(new WriterBuilder[0]);
        }
    }
    
    private static class UnclosableWriter extends Writer
    {
        private final Writer innerWritter;
        
        public UnclosableWriter(Writer innerWritter)
        {
            this.innerWritter = innerWritter;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException
        {
            innerWritter.write(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException
        {
            innerWritter.flush();
        }

        @Override
        public void close() throws IOException
        {
        }
    }
    
    private final Map<String, OutputConfigEntry> entries;
    private Writer systemOutput = new UnclosableWriter(new PrintWriter(System.out));
    private Writer systemErrorOutput = new UnclosableWriter(new PrintWriter(System.err));

    public OutputConfig()
    {
        this.entries = new HashMap<>();
    }
    
    public int getIdsCount()
    {
        return entries.size();
    }

    public String[] getIds()
    {
        return entries.keySet().toArray(new String[0]);
    }

    public boolean isPersistent(String id)
    {
        OutputConfigEntry entry = entries.get(id);

        if(entry == null)
            throw new NoSuchElementException("The id '" + id + "' has not been added to this configuration");

        return entry.isPermanent();
    }

    public WriterBuilder[] getBuildersFor(String id)
    {
        OutputConfigEntry entry = entries.get(id);

        if(entry == null)
            throw new NoSuchElementException("The id '" + id + "' has not been added to this configuration");

        return entry.getBuilders();
    }
    
    public OutputConfig setSystemOutput(Writer output)
    {
        if(output != null)
            systemOutput = output;
        
        return this;
    }
    
    public OutputConfig setSystemOutput(OutputStream output)
    {
        if(output != null)
            systemOutput = new PrintWriter(output);
        
        return this;
    }
    
    public OutputConfig setSystemErrorOutput(Writer output)
    {
        if(output != null)
            systemErrorOutput = output;
        
        return this;
    }
    
    public OutputConfig setSystemErrorOutput(OutputStream output)
    {
        if(output != null)
            systemErrorOutput = new PrintWriter(output);
        
        return this;
    }
    
    public OutputConfig addEntry(String id, OutputIdConfig idConfig)
    {
        if(id != null && idConfig != null)
        {
            OutputConfigEntry entry = getEntry(id);
            
            if(idConfig.isSystemOutputEnabled())
                entry.enableSystemOutput();
            
            if(idConfig.isSystemErrorOutputEnabled())
                entry.enableSystemErrorOutput();
            
            ArrayList<OutputIdConfig.FileEntry> fileEntries = idConfig.getFileEntries();
            
            for(OutputIdConfig.FileEntry fileEntry : fileEntries)
                entry.addFileOutput(fileEntry.path, fileEntry.append);
        }
        
        return this;
    }
    
    private OutputConfigEntry getEntry(String id)
    {
        OutputConfigEntry entry = entries.get(id);
        
        if(entry == null)
        {
            entry = new OutputConfigEntry();
            entries.put(id, entry);
        }
        
        return entry;
    }
    
    public OutputConfig addSystemOutputId(String id)
    {
        getEntry(id).enableSystemOutput();
        return this;
    }
    
    public OutputConfig addSystemErrorOutputId(String id)
    {
        getEntry(id).enableSystemErrorOutput();
        return this;
    }
    
    public OutputConfig addFileOutput(String id, String filePath)
    {
        return addFileOutput(id, () -> filePath);
    }
    
    public OutputConfig addFileOutput(String id, String filePath, boolean append)
    {
        return addFileOutput(id, () -> filePath, append);
    }
    
    public OutputConfig addFileOutput(String id, Supplier<String> filePathSupplier)
    {
        return addFileOutput(id, filePathSupplier, true);
    }
    
    public OutputConfig addFileOutput(String id, Supplier<String> filePathSupplier, boolean append)
    {
        getEntry(id).addFileOutput(filePathSupplier, append);
        return this;
    }
    
    public OutputConfig makePersistent(String id)
    {
        if(id != null)
            getEntry(id).makePersistent();
        
        return this;
    }
}
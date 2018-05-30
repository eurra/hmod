
package optefx.util.output;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Enrique Urra C.
 */
public class DefaultOutputManager extends OutputManager
{  
    private class MultiWriter extends Writer
    {
        private Writer[] writers = new Writer[0];
        private WriterBuilder[] builders;
        private boolean initialized;
        private boolean permanent;

        public void setPermanent()
        {
            this.permanent = true;
        }

        public boolean isPermanent()
        {
            return permanent;
        }
        
        public synchronized void setWriterBuilders(WriterBuilder[] builders)
        {
            if(builders == null)
                throw new NullPointerException("Null builders array");
            
            WriterBuilder[] newBuilders = new WriterBuilder[builders.length];

            for(int i = 0; i < builders.length; i++)
            {
                if(builders[i] == null)
                   throw new NullPointerException("Null builders at position " + i);

                newBuilders[i] = builders[i];
            }

            this.builders = newBuilders;
        }
        
        private synchronized void checkInitialized() throws IOException
        {
            if(!initialized && builders != null)
            {
                writers = new Writer[builders.length];
                
                for(int i = 0; i < builders.length; i++)
                {
                    WriterBuilder currBuilder = builders[i];
                    writers[i] = currBuilder.build();
                }
                
                initialized = true;
            }
        }

        @Override
        public void write(char[] chars, int i, int i1) throws IOException
        {
            checkInitialized();
            
            for(int j = 0; j < writers.length; j++)
            {
                writers[j].write(chars, i, i1);
                writers[j].flush();
            }
        }

        @Override
        public void flush() throws IOException
        {
            checkInitialized();
            
            for(int i = 0; i < writers.length; i++)
                writers[i].flush();
        }

        @Override
        public void close() throws IOException
        {
            checkInitialized();
            
            if(permanent)
                return;

            for(int i = 0; i < writers.length; i++)
                writers[i].close();
        }
    }
    
    private Map<String, MultiWriter> table;
    private Map<MultiWriter, PrintWriter> wrappers;

    public DefaultOutputManager()
    {
        this.table = new HashMap<>();
        this.wrappers = new HashMap<>();
    }

    @Override
    public DefaultOutputManager init()
    {
        return this;
    }
    
    private void addWrapper(MultiWriter writer)
    {
        PrintWriter pw = new PrintWriter(writer);
        wrappers.put(writer, pw);
    }
    
    private MultiWriter getWriter(String id)
    {
        MultiWriter writer = table.get(id);
            
        if(writer == null)
        {
            writer = new MultiWriter();
            addWrapper(writer);
            table.put(id, writer);
        }
        
        return writer;
    }
    
    @Override
    public DefaultOutputManager setOutputsFromConfig(OutputConfig config)
    {
        String[] ids = config.getIds();
        
        for(int i = 0; i < ids.length; i++)
        {
            String currId = ids[i];
            MultiWriter writer = getWriter(ids[i]);
            
            if(config.isPersistent(currId))
                writer.setPermanent();
            
            writer.setWriterBuilders(config.getBuildersFor(currId));
        }
        
        return this;
    }
    
    @Override
    public PrintWriter getOutput(String id)
    {
        MultiWriter writer = table.get(id);
        
        if(writer == null)
            return null;
        
        PrintWriter pw = wrappers.get(writer);
        return pw;
    }
    
    @Override
    public DefaultOutputManager closeOutputs()
    {        
        for(String id : table.keySet())
            closeOutputs(id);
        
        return this;
    }
    
    @Override
    public DefaultOutputManager closeOutputs(String id)
    {
        MultiWriter writer = table.get(id);
        
        try
        {
            if(writer != null)
                writer.close();
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Error while closing the '" + id + "' output", ex);
        }
        
        return this;
    }

    @Override
    public DefaultOutputManager clearOutputs()
    {
        ArrayList<String> idsToClear = new ArrayList<>();
        
        for(String id : table.keySet())
        {
            if(!table.get(id).isPermanent())
                idsToClear.add(id);
        }
        
        int count = idsToClear.size();
        
        for(int i = 0; i < count; i++)
        {
            MultiWriter writer = table.remove(idsToClear.get(i));
            wrappers.remove(writer);
        }
        
        return this;
    }
    
    @Override
    public DefaultOutputManager clearOutputs(String id)
    {
        MultiWriter writer = table.get(id);
        
        if(writer != null && !writer.isPermanent())
        {
            table.remove(id);
            wrappers.remove(writer);
        }
        
        return this;
    }
}

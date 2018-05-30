
package optefx.util.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Supplier;

/**
 *
 * @author Enrique Urra C.
 */
public final class FileWriterBuilder implements WriterBuilder
{
    private Supplier<String> pathSupplier;
    private boolean append;

    public FileWriterBuilder(String path)
    {
        this(() -> path);
    }
    
    public FileWriterBuilder(Supplier<String> pathSupplier)
    {
        if(pathSupplier == null)
            throw new NullPointerException("Null path");
        
        this.pathSupplier = pathSupplier;
    }
    
    public FileWriterBuilder enableAppend(boolean enable)
    {
        this.append = enable;
        return this;
    }

    @Override
    public Writer build() throws IOException
    {
        String path = pathSupplier.get();
        File file = new File(path);
        File dir = file.getParentFile();
        
        if(dir != null)
        {
            dir.mkdirs();
        
            if(!dir.exists())
                throw new IOException("Cannot generate directories for the path '" + pathSupplier + "'");
        }
        
        FileWriter fileWriter = null;
        
        try
        {
            fileWriter = new FileWriter(path, append);
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Error while creating output file.", ex);
        }
        
        return fileWriter;
    }
}

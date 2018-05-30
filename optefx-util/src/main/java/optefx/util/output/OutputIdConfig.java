
package optefx.util.output;

import java.util.ArrayList;
import java.util.function.Supplier;

/**
 *
 * @author Enrique Urra C.
 */
public final class OutputIdConfig
{
    class FileEntry
    {
        public Supplier<String> path;
        public boolean append;

        public FileEntry(Supplier<String> path, boolean append)
        {
            this.path = path;
            this.append = append;
        }
    }
    
    private boolean systemOutput;
    private boolean systemErrorOutput;
    private final ArrayList<FileEntry> fileEntries;

    public OutputIdConfig()
    {
        this.fileEntries = new ArrayList<>();
    }
    
    public OutputIdConfig enableSystemOutput()
    {
        systemOutput = true;
        return this;
    }
    
    public OutputIdConfig enableSystemErrorOutput()
    {
        systemErrorOutput = true;
        return this;
    }
    
    boolean isSystemOutputEnabled()
    {
        return systemOutput;
    }

    boolean isSystemErrorOutputEnabled()
    {
        return systemErrorOutput;
    }
    
    public OutputIdConfig addFileOutput(String filePath)
    {
        return addFileOutput(() -> filePath);
    }
    
    public OutputIdConfig addFileOutput(String filePath, boolean append)
    {
        return addFileOutput(() -> filePath, append);
    }
    
    public OutputIdConfig addFileOutput(Supplier<String> filePathSupplier)
    {
        return addFileOutput(filePathSupplier, true);
    }
    
    public OutputIdConfig addFileOutput(Supplier<String> filePathSupplier, boolean append)
    {
        fileEntries.add(new FileEntry(filePathSupplier, append));
        return this;
    }
    
    ArrayList<FileEntry> getFileEntries()
    {
        return fileEntries;
    }
}

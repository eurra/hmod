
package optefx.util.output;

import java.io.PrintWriter;

/**
 *
 * @author Enrique Urra C.
 */
public final class OutputPrinter
{
    private final OutputManager manager;
    private final String outputId;

    public OutputPrinter(OutputManager manager, String outputId)
    {
        this.manager = manager;
        this.outputId = outputId;
    }

    public PrintWriter getPrinter()
    {
        return manager.getOutput(outputId);
    }

    public void print(Object obj)
    {
        PrintWriter pw = manager.getOutput(outputId);
        
        if(pw != null)
            pw.print(obj);
    }

    public void println(Object obj)
    {
        PrintWriter pw = manager.getOutput(outputId);
        
        if(pw != null)
            pw.println(obj);
    }

    public void println()
    {
        PrintWriter pw = manager.getOutput(outputId);
        
        if(pw != null)
            pw.println();
    }

    public void format(String format, Object... objs)
    {
        PrintWriter pw = manager.getOutput(outputId);
        
        if(pw != null)
            pw.format(format, objs);
    }
    
    public void close()
    {
        manager.closeOutputs(outputId);
    }
}

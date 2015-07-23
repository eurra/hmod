
package hmod.launcher;

import hmod.core.AlgorithmException;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author Enrique Urra C.
 */
class AlgorithmFrameWriter extends Writer
{
    /*private final Consumer<String> input;
    private final StringBuffer buffer = new StringBuffer();
    private long timeStamp = System.currentTimeMillis();
    
    public AlgorithmFrameWriter(Consumer<String> input)
    {
        this.input = input;
    }*/
    
    private final JTextArea textArea;
    
    public AlgorithmFrameWriter(JTextArea textArea)
    {
        this.textArea = textArea;
    }
    
    @Override
    public void write(char[] chars, int i, int i1)
    {
        try
        {   
            SwingUtilities.invokeAndWait(() -> {
                textArea.append(String.copyValueOf(chars, i, i1));

                if(textArea.getLineCount() > 1000)
                {
                    try
                    {
                        int lastOffset = textArea.getLineEndOffset(textArea.getLineCount() - 1000);
                        textArea.replaceRange("", 0, lastOffset);
                    }
                    catch(BadLocationException ex)
                    {
                        throw new AlgorithmException(ex);
                    }
                }
            });
        }
        catch(InterruptedException | InvocationTargetException ex)
        {
        }
        
        //input.accept(String.copyValueOf(chars, i, i1));
        //buffer.append(chars, i, i1);
    }  
    
    @Override
    public void flush() throws IOException
    {
        //flush(System.currentTimeMillis());
    }

    /*@Override
    public void write(char[] cbuf, int off, int len) throws IOException
    {
        buffer.append(cbuf, off, len);
        //flush(-1);
    }
    
    @Override
    public void flush() throws IOException
    {
        //flush(System.currentTimeMillis());
        //if(System.currentTimeMillis() - timeStamp > 50)
        //{
            input.accept(buffer.toString());
            //buffer.setLength(0);

            //if(currTime > -1)
        //        timeStamp = System.currentTimeMillis();
        //}
    }*/
    
    /*private void flush(boolean force)
    {
        if(force || (System.currentTimeMillis() - timeStamp > 50))
        {
            input.accept(buffer.toString());
            //buffer.setLength(0);

            //if(currTime > -1)
                timeStamp = System.currentTimeMillis();
        }
    }*/
    
    @Override
    public void close() throws IOException
    {
        //flush(true);
        //flush();
    }
}

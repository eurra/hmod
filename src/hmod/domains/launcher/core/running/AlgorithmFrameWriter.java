
package hmod.domains.launcher.core.running;

import java.io.IOException;
import java.io.Writer;
import javax.swing.JTextArea;

/**
 *
 * @author Enrique Urra C.
 */
class AlgorithmFrameWriter extends Writer
{
    private JTextArea textArea;

    public AlgorithmFrameWriter(JTextArea textArea)
    {
        this.textArea = textArea;
    }

    @Override
    public void write(char[] chars, int i, int i1)
    {
        textArea.append(String.copyValueOf(chars, i, i1));
    }

    @Override
    public void flush() throws IOException
    {
    }

    @Override
    public void close() throws IOException
    {
    }
}

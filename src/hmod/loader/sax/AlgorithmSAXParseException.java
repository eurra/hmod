
package hmod.loader.sax;

import org.xml.sax.SAXException;

/**
 * Implements an exception related to errors in the parsing of XML algorithm 
 * definition files.
 * @author Enrique Urra C.
 */
public class AlgorithmSAXParseException extends SAXException
{
    /**
     * Stores the XML line number of the error.
     */
    private int line;
    /**
     * Store the XML file name.
     */
    private String file;
    
    /**
     * Constructor, which builds from another exception.
     * @param line The line of the error.
     * @param file The file of the error.
     * @param e The base exception.
     */
    public AlgorithmSAXParseException(int line, String file, Exception e)
    {
        super(e);
        
        this.line = line;
        this.file = file;
    }

    /**
     * Constructor, which provide an exception message.
     * @param line The line of the error.
     * @param file The file of the error.
     * @param message The string message.
     */
    public AlgorithmSAXParseException(int line, String file, String message)
    {
        super(message);
        
        this.line = line;
        this.file = file;
    }

    /**
     * Constructor with a message and a parent exception.
     * @param line The line of the error.
     * @param file The file of the error.
     * @param message The string message.
     * @param e The base exception.
     */
    public AlgorithmSAXParseException(int line, String file, String message, Exception e)
    {
        super(message, e);
        
        this.line = line;
        this.file = file;
    }
    
    /**
     * Gets the line of the error.
     * @return The line number or -1 of it has not been specified.
     */
    public int getLine()
    {
        return line;
    }

    /**
     * Gets the XML file name.
     * @return The file name.
     */
    public String getFile()
    {
        return file;
    }

    @Override
    public String getLocalizedMessage()
    {
        if(line > 0)
            return "[Line " + line + (file != null ? ": " + file : "") + "] " + super.getLocalizedMessage();
        else
            return super.getLocalizedMessage();
    }
}

package hmod.loader.sax;

import mobs.core.ObjectTreeBuilder;
import hmod.parser.ObjectBuildParser;
import mobs.ObjectBuildException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Implements an  algorithm definition container which parse a XMl file using 
 * the SAX library.
 * @author Enrique Urra C.
 */
final class SAXContainer extends ObjectBuildParser
{
    /**
     * The internal contentHandler used.
     */
    private SAXParseHandler handler;
    /**
     * The context manager used.
     */
    private AlgorithmContextManager contextManager;
    /**
     * The internal reader used by the cointainer;
     */
    private XMLReader xmlReader;
    /**
     * The id used as the initial step.
     */
    private String startId = "!start"; 
    
    /**
     * Constructor.
     * @param builder The builder object to use internally.
     * @param handler The internal parse handler.
     * @param contextManager The context manager to use.
     * @throws ObjectBuildException If any error occurs in the initialization
     */
    public SAXContainer(ObjectTreeBuilder builder, SAXParseHandler handler, AlgorithmContextManager contextManager) throws ObjectBuildException
    { 
        super(builder);
        
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        
        try
        {
            SAXParser saxParser = spf.newSAXParser();
            xmlReader = saxParser.getXMLReader();
        }
        catch(ParserConfigurationException | SAXException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        this.handler = handler;
        this.xmlReader.setContentHandler(this.handler);
        this.contextManager = contextManager;
    }
    
    /**
     * Gets the internal parse handler.
     * @return The parse handler object.
     */
    public SAXParseHandler getHandler()
    {
        return handler;
    }

    /**
     * Gets the internal context manager.
     * @return The manager object.
     */
    public AlgorithmContextManager getContextManager()
    {
        return contextManager;
    }
    
    /**
     * Sets the starting id.
     * @param startId The id to use.
     */
    public void setStartId(String startId)
    {
        this.startId = startId;
    }
    
    /**
     * Gets the starting id.
     * @return The id used.
     */
    public String getStartId()
    {
        return startId;
    }
    
    @Override
    public void parseInput(String input) throws ObjectBuildException
    {
        handler.setCurrentInput(input);
        
        try
        {
            File fileCheck = new File(input);
            
            if(!fileCheck.exists())
            {
                InputStream is = SAXContainer.class.getResourceAsStream(input);
                
                if(is == null)
                    throw new ObjectBuildException("The provided input was not found (" + input + ")");
                else
                    xmlReader.parse(new InputSource(is));
            }
            else
            {
                xmlReader.parse(input);
            }
        }
        catch(IOException | SAXException ex)
        {
            throw new ObjectBuildException(ex);
        }
    }
    
    @Override
    public void restart()
    {
        super.restart();
        contextManager.reset();
    }
}
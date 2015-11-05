
package hmod.parser.sax;

import mobs.ObjectBuildException;
import hmod.parser.AlgorithmParserException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Implements an SAX parse handler to process a XML algorithm definition.
 * @author Enrique Urra C.
 */
final class SAXParseHandler extends DefaultHandler
{
    /**
     * A table that stores the parseable tags and the processors that parse 
     * them.
     */
    private HashMap<String, SAXTagParser> parsersTable;
    /**
     * A list of the parsers, without reference repetitions.
     */
    private ArrayList<SAXTagParser> parsersList;
    /**
     * A stack of tags and their attributes that will be used in the parsing
     * process.
     */
    private ArrayList<HashMap<String, String>> tagStack;
    /**
     * The document locator (to handle errors).
     */
    private Locator locator;
    /**
     * The input file used in the parsing.
     */
    private String input;
        
    /**
     * Default Constructor.
     */
    public SAXParseHandler()
    {
        this.parsersTable = new HashMap<String, SAXTagParser>();
        this.parsersList = new ArrayList<SAXTagParser>();
        this.tagStack = new ArrayList<HashMap<String, String>>();
    }
    
    /**
     * Registers an individual tag parser.
     * @param parser The parser object.
     * @throws ObjectBuildException if the provided parser is invalid or contains 
     *  invalid tag definitions.
     */
    public void registerTagParser(SAXTagParser parser) throws ObjectBuildException
    {
        if(parser == null)
            throw new ObjectBuildException("A provided tag parser is null");
        
        String[] tags = parser.getParseableTags();
        
        if(tags == null || tags.length == 0)
            throw new ObjectBuildException("There are no valid tags from the provided parser");
        
        for(String tag : tags)
        {
            if(tag == null)
                throw new ObjectBuildException("The provided parseable tags cannot be null");
            
            parsersTable.put(tag, parser);
        }
        
        if(!parsersList.contains(parser))
            parsersList.add(parser);
    }
    
    /**
     * Sets the current input file.
     * @param input The file path.
     */
    public void setCurrentInput(String input)
    {
        this.input = input;
    }

    @Override
    public void setDocumentLocator(Locator locator)
    {
        super.setDocumentLocator(locator);
        this.locator = locator;
    }
    
    @Override
    public void startDocument() throws AlgorithmParserException
    {
        try
        {
            for(SAXTagParser parser : parsersList)
                parser.startDefinition();
        }
        catch(ObjectBuildException ex)
        {
            throw new AlgorithmParserException(0, input, ex);
        }
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws AlgorithmParserException
    {  
        int count = attributes.getLength();
        HashMap<String, String> attrMap = new HashMap<String, String>(count);
        
        for(int i = 0; i < count; i++)
            attrMap.put(attributes.getLocalName(i), attributes.getValue(i));
        
        tagStack.add(attrMap);
        SAXTagParser parser = parsersTable.get(localName);
        
        if(parser != null)
        {
            try
            {
                parser.parseTagStart(localName, attrMap);
            }
            catch(ObjectBuildException ex)
            {
                throw new AlgorithmParserException(locator.getLineNumber(), input, ex);
            }
        }
        else if(!localName.equals("algorithmDefinition"))
        {
            throw new AlgorithmParserException(locator.getLineNumber(), input, "'" + localName + "': such element tag is not supported");
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws AlgorithmParserException
    {
        int tagStackSize = tagStack.size();
        HashMap<String, String> attrMap = tagStack.get(tagStackSize - 1);
        SAXTagParser parser = parsersTable.get(localName);
        
        if(parser != null)
        {
            try
            {
                parser.parseTagEnd(localName, attrMap);
            }
            catch(ObjectBuildException ex)
            {
                throw new AlgorithmParserException(locator.getLineNumber(), input, ex);
            }
        }
        else if(!localName.equals("algorithmDefinition"))
        {
            throw new AlgorithmParserException(locator.getLineNumber(), input, "'" + localName + "': such element tag is not supported");
        }
        
        tagStack.remove(tagStackSize - 1);
    }

    @Override
    public void endDocument() throws AlgorithmParserException
    {
        try
        {
            for(SAXTagParser parser : parsersList)
                parser.endDefinition();
        }
        catch(ObjectBuildException ex)
        {
            throw new AlgorithmParserException(0, input, ex);
        }
    }
}
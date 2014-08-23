
package hmod.loader.sax;

import mobs.ObjectBuildException;
import java.util.Map;

/**
 * Defines a tag processor to handle custom tags on an algorithm definition XML.
 * @author Enrique Urra C.
 */
public interface SAXTagParser
{
    /**
     * Runs at the start of a file parsing.
     */
    void startDefinition() throws ObjectBuildException;
    
    /**
     * Parses the information of a tag on its start.
     * @param tagName The related tag.
     * @param attributes The attributes as map.
     * @throws ObjectBuildException if an error occurs during the parsing.
     */
    void parseTagStart(String tagName, Map<String, String> attributes) throws ObjectBuildException;
    
    /**
     * Parses the information of a tag on its end.
     * @param tagName The related tag.
     * @param attributes The attributes as map.
     * @throws ObjectBuildException if an error occurs during the parsing.
     */
    void parseTagEnd(String tagName, Map<String, String> attributes) throws ObjectBuildException;
    
    /**
     * Runs at the ending of a file parsing.
     * @throws ObjectBuildException if an error occurs during the parsing.
     */
    void endDefinition() throws ObjectBuildException;
    
    /**
     * Gets an array with the parseable tags.
     * @return An array with the tags.
     */
    String[] getParseableTags();
}

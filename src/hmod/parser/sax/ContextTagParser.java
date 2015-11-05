
package hmod.parser.sax;

import mobs.ObjectBuildException;
import java.util.Map;

/**
 * Implements a tag parser for algorithm-context entities.
 * @author Enrique Urra C.
 */
class ContextTagParser implements SAXTagParser
{
    /**
     * The internal context manager.
     */
    private AlgorithmContextManager contextManager;
    private String currContextId;

    public ContextTagParser(AlgorithmContextManager contextManager)
    {
        this.contextManager = contextManager;
    }

    @Override
    public void startDefinition() throws ObjectBuildException
    {
        currContextId = null;
    }

    @Override
    public void parseTagStart(String tagName, Map<String, String> attributes) throws ObjectBuildException
    {
        if(tagName.equals("algorithmContext"))
            startAlgorithmContext(attributes.get("id"));
        else if(tagName.equals("contextDef"))
            parseDefinition(attributes.get("id"), attributes.get("class"));
    }
    
    private void startAlgorithmContext(String id) throws ObjectBuildException
    {
        if(id == null)
            throw new ObjectBuildException("The id of an algorithm context must be provided");
        
        if(currContextId != null)
            throw new ObjectBuildException("Another algorithm context is currently being processed");
        
        currContextId = id;
    }
    
    private void parseDefinition(String id, String className) throws ObjectBuildException
    {
        if(currContextId == null)
            throw new ObjectBuildException("No algorithm context is currently being processed");
        
        if(id == null)
            throw new ObjectBuildException("An id was expected");
        
        if(className == null)
            throw new ObjectBuildException("An class name was expected");
        
        contextManager.addDefinition(currContextId, id, className);
    }

    @Override
    public void parseTagEnd(String tagName, Map<String, String> attributes) throws ObjectBuildException
    {
        if(tagName.equals("algorithmContext"))
            endAlgorithmContext();
    }
    
    private void endAlgorithmContext() throws ObjectBuildException
    {
        if(currContextId == null)
            throw new ObjectBuildException("No algorithm context is currently being processed");
        
        currContextId = null;
    }

    @Override
    public void endDefinition() throws ObjectBuildException
    {        
        if(currContextId != null)
            throw new ObjectBuildException("A context is currently active");
    }

    @Override
    public String[] getParseableTags()
    {
        return new String[] {
            "algorithmContext",
            "contextDef"
        };
    }
}

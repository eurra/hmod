
package hmod.parser.sax;

import java.util.HashMap;

/**
 * Implements an algorithm context, for generalization parsing tasks.
 * @author Enrique Urra C.
 */
class AlgorithmContext
{
    private String id;
    private HashMap<String, Class> defs;

    public AlgorithmContext(String id)
    {
        this.id = id;
        this.defs = new HashMap<String, Class>();
    }

    public String getId()
    {
        return id;
    }
    
    public void addDefinition(String id, Class definition)
    {
        defs.put(id, definition);
    }
    
    public Class getDefinition(String id)
    {
        return defs.get(id);
    }
}
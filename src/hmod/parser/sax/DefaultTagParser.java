
package hmod.parser.sax;

import mobs.core.SetterFactory;
import mobs.core.ObjectTreeBuilder;
import mobs.ObjectBuildException;
import hMod.exception.ReflectionException;
import hmod.util.ReflectionTool;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Implements the basic algorithm definition parser, which process some default
 * tags related directly to the basic entities.
 * @author Enrique Urra C.
 */
class DefaultTagParser implements SAXTagParser
{    
    /**
     * The internal builder.
     */
    private ObjectTreeBuilder builder;

    public DefaultTagParser(ObjectTreeBuilder builder)
    {
        if(builder == null)
            throw new NullPointerException("The builder cannot be null");
        
        this.builder = builder;
    }
    
    @Override
    public void startDefinition() throws ObjectBuildException
    {
    }
    
    @Override
    public void parseTagStart(String tagName, Map<String, String> attributes) throws ObjectBuildException
    {
        if(tagName.equals("bean"))
            startParseBean(attributes.get("id"), attributes.get("desc"), attributes.get("class"));
        else if(tagName.equals("array"))
            startParseArray(attributes.get("id"), attributes.get("desc"), attributes.get("class"), attributes.get("length"), attributes.get("arrDepth"));
        else if(tagName.equals("ref"))
            parseReference(attributes.get("dest"), attributes.get("desc"));
        else if(tagName.equals("prim"))
            parsePrimitive(attributes.get("id"), attributes.get("desc"), attributes.get("class"), attributes.get("val"));
        else if(tagName.equals("beanSetter"))
            parseBeanSetter(attributes.get("name"));
        else if(tagName.equals("arraySetter"))
            parseArraySetter(attributes.get("pos"));
        else if(tagName.equals("field"))
            parseFieldStart(attributes.get("set"), attributes);
    }
    
    private void startParseBean(String id, String desc, String className) throws ObjectBuildException
    {        
        if(className == null)
            throw new ObjectBuildException("A class name was expected");
        
        Object obj = null;
        
        try
        {
            obj = ReflectionTool.createObject(className);
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        builder.startEntity(id, obj, desc);
    }
    
    private void startParseArray(String id, String desc, String className, String lengthStr, String arrayDepthStr) throws ObjectBuildException
    {        
        if(className == null)
            throw new ObjectBuildException("A class name was expected");
        
        if(lengthStr == null)
            throw new ObjectBuildException("An array length was expected");
        
        Object obj = null;
        int length = 0, arrayDepth = 0;
        
        try
        {
            length = ReflectionTool.parseInt(lengthStr);
            
            if(arrayDepthStr != null)
                arrayDepth = ReflectionTool.parseInt(arrayDepthStr);
            
            obj = ReflectionTool.createArray(className, length, arrayDepth);
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        builder.startEntity(id, obj, desc);
    }
    
    private void parseReference(String dest, String desc) throws ObjectBuildException
    {
        builder.addReference(dest, desc);
    }
    
    private void parsePrimitive(String id, String desc, String className, String val) throws ObjectBuildException
    {
        if(className == null)
            throw new ObjectBuildException("A primitive must define its class type");
        
        if(val == null)
            throw new ObjectBuildException("A primitive must define its value");
        
        Object obj = null;
        
        try
        {
            obj = ReflectionTool.createObject(className, val);
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        builder.startEntity(id, obj, desc).endEntity();
    }
    
    private void parseBeanSetter(String name) throws ObjectBuildException
    {        
        if(name == null)
            throw new ObjectBuildException("A setter name was expected");

        Class currEntityClass = builder.getCurrentObject().getClass();
        Method setter = null;
        
        try
        {
            setter = ReflectionTool.extractSetter(currEntityClass, name);
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        builder.addSetter(null, SetterFactory.createBeanSetter(setter));
    }
    
    private void parseArraySetter(String posStr) throws ObjectBuildException
    {        
        if(posStr == null)
            throw new ObjectBuildException("A position number was expected");
        
        int pos = 0;
        
        try
        {
            pos = ReflectionTool.parseInt(posStr);
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        builder.addSetter(null, SetterFactory.createArraySetter(pos));
    }
    
    private void parseFieldStart(String setType, Map<String, String> attributes) throws ObjectBuildException
    {        
        if(setType == null)
            throw new ObjectBuildException("A set type must be specified");
        
        Object currObject = builder.getCurrentObject();
        
        if(currObject == null)
            throw new ObjectBuildException("No element is currently active to start a field.");

        if(!currObject.getClass().isArray())
            parseBeanSetter(attributes.get("name"));
        else
            parseArraySetter(attributes.get("pos"));
        
        String valClassAttr = attributes.get("valClass"); 
        String arrayAttr = attributes.get("arrDepth");
        int arrayDepth = 0;
        
        try
        {
            if(arrayAttr != null)
                arrayDepth = ReflectionTool.parseInt(arrayAttr);
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        if(arrayDepth > 0)
            arrayDepth--;
        
        if(setType.equals("bean"))
            startParseBean(null, null, valClassAttr);
        else if(setType.equals("array"))
            startParseArray(null, null, valClassAttr, attributes.get("length"), ("" + arrayDepth));
        else if(setType.equals("ref"))
            parseReference(attributes.get("dest"), null);
        else if(setType.equals("prim"))
            parsePrimitive(null, null, valClassAttr, attributes.get("val"));
        else
            throw new ObjectBuildException("Such argument type is not supported");
    }
    
    @Override
    public void parseTagEnd(String tagName, Map<String, String> attributes) throws ObjectBuildException
    { 
        if(tagName.equals("bean"))
            endParseBean();
        else if(tagName.equals("array"))
            endParseArray();
        else if(tagName.equals("field"))
            parseFieldEnd(attributes.get("set"));
    }
    
    private void endParseBean() throws ObjectBuildException
    {        
        Object currObject = builder.getCurrentObject();
        
        if(currObject == null)
            throw new ObjectBuildException("No bean is currently active.");
        
        if(currObject.getClass().isArray())
            throw new ObjectBuildException("Cannot end bean: an array is currently active.");
        
        builder.endEntity();
    }
    
    private void endParseArray() throws ObjectBuildException
    {
        Object currObject = builder.getCurrentObject();
        
        if(currObject == null)
            throw new ObjectBuildException("No array is currently active.");
        
        if(!currObject.getClass().isArray())
            throw new ObjectBuildException("Cannot end array: a bean is currently active.");
        
        builder.endEntity();
    }
    
    private void parseFieldEnd(String setType) throws ObjectBuildException
    {
        if(setType == null)
            throw new ObjectBuildException("An argument type must be specified");
        
        if(setType.equals("bean"))
            endParseBean();
        else if(setType.equals("array"))
            endParseArray();
    }
    
    @Override
    public void endDefinition() throws ObjectBuildException
    {
    }
    
    @Override
    public String[] getParseableTags()
    {
        return new String[] {
            "bean",
            "array",
            "ref",
            "prim",
            "beanSetter",
            "arraySetter",
            "field"
        };
    }
}
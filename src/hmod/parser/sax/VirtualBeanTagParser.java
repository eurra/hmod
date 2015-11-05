
package hmod.parser.sax;

import mobs.core.ObjectTreeBuilder;
import mobs.ObjectBuildException;
import hMod.exception.ReflectionException;
import hmod.util.ReflectionTool;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * @author Enrique Urra C.
 */
class VirtualBeanTagParser implements SAXTagParser
{
    private class virtualBeanData
    {
        String id;
        String desc;
        ArrayList<Class> virtualBeanInterfaces;
    }
    
    private virtualBeanData virtualBeanData;
    private AlgorithmContextManager contextManager;
    private ObjectTreeBuilder builder;

    public VirtualBeanTagParser(ObjectTreeBuilder builder, AlgorithmContextManager contextManager)
    {
        if(contextManager == null)
            throw new NullPointerException("The provided context manager cannot be null");
        
        if(builder == null)
            throw new NullPointerException("The builder cannot be null");
        
        this.builder = builder;
        this.contextManager = contextManager;
    }
    
    @Override
    public void startDefinition() throws ObjectBuildException
    {
        virtualBeanData = null;
    }

    @Override
    public void parseTagStart(String tagName, Map<String, String> attributes) throws ObjectBuildException
    {
        if(tagName.equals("vBean"))
            startVirtualBean(attributes.get("id"), attributes.get("desc"));
        else if(tagName.equals("vBeanDef"))
            startVirtualBeanDefs();
        else if(tagName.equals("vBeanType"))
            parseVirtualBeanType(attributes.get("class"), attributes.get("def"));
    }
    
    private void startVirtualBean(String id, String desc) throws ObjectBuildException
    {
        if(virtualBeanData != null)
            throw new ObjectBuildException("Another virtual bean is being processed");
        
        virtualBeanData = new virtualBeanData();
        virtualBeanData.id = id;
        virtualBeanData.desc = desc;
    }
    
    private void startVirtualBeanDefs() throws ObjectBuildException
    {
        if(virtualBeanData == null)
            throw new ObjectBuildException("A virtual bean must be in processing");
        
        virtualBeanData.virtualBeanInterfaces = new ArrayList<Class>();
    }
    
    private void parseVirtualBeanType(String className, String def) throws ObjectBuildException
    {
        if(virtualBeanData == null)
            throw new ObjectBuildException("A virtual bean must be in processing");
        
        if(virtualBeanData.virtualBeanInterfaces == null)
            throw new ObjectBuildException("A virtual bean definition section must be started");
        
        if(className == null && def == null)
            throw new ObjectBuildException("A class (interface) name or definition id was expected");
        
        Class vbClass = null;
        
        if(def != null)
        {
            vbClass = contextManager.getDefinition(def);
        }
        else
        {
            try
            {
                vbClass = ReflectionTool.getClassFromString(className);
            }
            catch(ReflectionException ex)
            {
                throw new ObjectBuildException(ex);
            }
        }
        
        virtualBeanData.virtualBeanInterfaces.add(vbClass);
    }
    
    @Override
    public void parseTagEnd(String tagName, Map<String, String> attributes) throws ObjectBuildException
    {        
        if(tagName.equals("vBean"))
            builder.endEntity();
        else if(tagName.equals("vBeanDef"))
            endVirtualBean();
    }
    
    private void endVirtualBean() throws ObjectBuildException
    {
        if(virtualBeanData == null)
            throw new ObjectBuildException("A virtual bean must be in processing");
        
        if(virtualBeanData.virtualBeanInterfaces == null)
            throw new ObjectBuildException("A virtual bean definition section must be started");
        
        Class[] interfacesClassObjs = new Class[virtualBeanData.virtualBeanInterfaces.size()];
        int index = 0;
        
        for(Class interfaceClass : virtualBeanData.virtualBeanInterfaces)
            interfacesClassObjs[index++] = interfaceClass;
        
        Object obj = null;
        
        try
        {
            Class classObj = Proxy.getProxyClass(getClass().getClassLoader(), interfacesClassObjs);
            obj = ReflectionTool.createObject(classObj, new VirtualBeanHandler());
        }
        catch(IllegalArgumentException ex)
        {
            throw new ObjectBuildException("The provided interface set is invalid", ex);
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        String id = virtualBeanData.id;
        String desc = virtualBeanData.desc;
        virtualBeanData = null;
        
        builder.startEntity(id, obj, desc);
    }
    
    @Override
    public void endDefinition() throws ObjectBuildException
    {
    }
        
    @Override
    public String[] getParseableTags()
    {
        return new String[] {
            "vBean",
            "vBeanDef",
            "vBeanType"
        };
    }
}
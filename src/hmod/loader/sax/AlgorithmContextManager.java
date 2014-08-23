
package hmod.loader.sax;

import hmod.core.ContextDefinition;
import mobs.ObjectBuildException;
import hMod.exception.ReflectionException;
import hmod.util.ReflectionTool;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

/**
 * Implements an algorithm context manager.
 * @author Enrique Urra C.
 */
final class AlgorithmContextManager
{
    private HashMap<String, AlgorithmContext> staticContexts;
    private HashMap<String, AlgorithmContext> dynamicContexts;

    public AlgorithmContextManager() throws ObjectBuildException
    {
        staticContexts = new HashMap<String, AlgorithmContext>();
        dynamicContexts = new HashMap<String, AlgorithmContext>();
        
        loadStaticContexts();
    }
    
    private AlgorithmContext getOrCreateContext(String contextId, boolean mutable)
    {
        if(mutable)
            return getOrCreateContext(contextId, dynamicContexts);
        else
            return getOrCreateContext(contextId, staticContexts);
    }
    
    private AlgorithmContext getOrCreateContext(String contextId, HashMap<String, AlgorithmContext> contextsMap)
    {
        AlgorithmContext context = contextsMap.get(contextId);
        
        if(context == null)
        {
            context = new AlgorithmContext(contextId);
            contextsMap.put(contextId, context);
        }
        
        return context;
    }
    
    private String[] checkContextId(String def) throws ObjectBuildException
    {
        if(def == null)
            throw new ObjectBuildException("The provided context id is null");
        
        String[] defSplit = def.split("/");
            
        if(defSplit.length != 2)
            throw new ObjectBuildException("Wrong context id '" + def + "' (format: <context>/<id>)");
        
        return defSplit;
    }
    
    public void loadStaticContexts() throws ObjectBuildException
    {
        ClassLoader cl = ClassLoader.getSystemClassLoader(); 
        URL[] urls = ((URLClassLoader)cl).getURLs();
        Set<URL> urlSet = new HashSet<URL>();
        urlSet.addAll(Arrays.asList(urls));
        
        Reflections reflections = new Reflections(new ConfigurationBuilder()
            //.filterInputsBy(filter)
            //.setUrls(ClasspathHelper.forClassLoader(ClasspathHelper.contextClassLoader()))
            //.setUrls(ClasspathHelper.forPackage("hMod.hyperheuristic"))
            .setUrls(urls)
            .setScanners(new TypeAnnotationsScanner()));
        
        loadDefinitionsFromPackages(reflections.getTypesAnnotatedWith(ContextDefinition.class));
    }
    
    private void loadDefinitionsFromPackages(Set<Class<?>> subTypes) throws ObjectBuildException
    {
        for(Class<?> classObj : subTypes)
        {            
            ContextDefinition oc = classObj.getAnnotation(ContextDefinition.class);
            
            if(oc != null)
                addDefinition(oc.context(), oc.id(), classObj.getName(), false);
        }
    }
    
    public void addDefinition(String contextId, String opDefId, String className) throws ObjectBuildException
    {
        addDefinition(contextId, opDefId, className, true);
    }
    
    public void addDefinition(String contextId, String opDefId, String className, boolean mutable) throws ObjectBuildException
    {
        Class opClass = null;
        
        try
        {
            opClass = ReflectionTool.getClassFromString(className);
        }
        catch(ReflectionException ex)
        {
            throw new ObjectBuildException(ex);
        }
        
        AlgorithmContext context = getOrCreateContext(contextId, mutable);
        context.addDefinition(opDefId, opClass);
    }
    
    private Class getDefinition(String contextId, String id, HashMap<String, AlgorithmContext> contextTable)
    {
        AlgorithmContext context = contextTable.get(contextId);
        
        if(context != null)
            return context.getDefinition(id);
        
        return null;
    }
    
    public Class getDefinition(String def) throws ObjectBuildException
    {        
        String[] defSplit = checkContextId(def);
        return getDefinition(defSplit[0], defSplit[1]);
    }
    
    public Class getDefinition(String contextId, String id) throws ObjectBuildException
    {
        Class ret = getDefinition(contextId, id, dynamicContexts);
        
        if(ret == null)
            ret = getDefinition(contextId, id, staticContexts);
        
        if(ret == null)
            throw new ObjectBuildException("Context id not found: '" + contextId + "/" + id + "'");
        
        return ret;
    }
    
    public void reset()
    {
        dynamicContexts.clear();
    }
}
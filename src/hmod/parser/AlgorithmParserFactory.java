
package hmod.parser;

import hmod.parser.builders.TreeBuilderParserFactory;
import optefx.util.reflection.ReflectionException;
import optefx.util.reflection.ReflectionTool;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a factory type for algorithm parsers.
 * @author Enrique Urra C.
 */
public abstract class AlgorithmParserFactory<T extends AlgorithmParser>
{
    private static final Class defaultFactory = TreeBuilderParserFactory.class;
    private static final Map<Class, AlgorithmParserModuleInfo> moduleInfos;
    private static final Map<Class, AlgorithmParserFactory> factories;
    
    static
    {
        moduleInfos = new HashMap<>();
        factories = new HashMap<>();

        // Here, the parser modules are loaded
        Package[] pkgs = Package.getPackages();

        for(int i = 0; i < pkgs.length; i++)
        {
            String name = pkgs[i].getName();
            String[] nameSplit = name.split("\\.");
            boolean annotated = pkgs[i].isAnnotationPresent(AlgorithmParserModuleInfo.class);

            if(name.startsWith("hmod.parser.") && nameSplit.length == 3 && annotated)
            {
                AlgorithmParserModuleInfo info = pkgs[i].getAnnotation(AlgorithmParserModuleInfo.class);

                if(info == null)
                    throw new RuntimeException("The '" + name + "' parser module must be annotated with a '" + AlgorithmParserModuleInfo.class.getName() + "' entry");

                Class<? extends AlgorithmParserFactory> factoryType = info.factoryType();

                if(factories.containsKey(info.factoryType()))
                    throw new RuntimeException("The factory type (" + factoryType.getName() + ") defined by the '" + name + "' parser module is already registered");

                AlgorithmParserFactory factory = null;

                try
                {
                    factory = (AlgorithmParserFactory)ReflectionTool.createObject(info.factoryType());
                }
                catch(ReflectionException ex)
                {
                    throw new RuntimeException("Error while initializing the '" + name + "' module: " + ex.getLocalizedMessage(), ex);
                }

                moduleInfos.put(factoryType, info);
                factories.put(factoryType, factory);
            }
        }
        
        if(moduleInfos.isEmpty())
            throw new RuntimeException("No parser module has been loaded in the classpath");
    }
    
    public static AlgorithmParserFactory getDefaultFactory() throws AlgorithmParserException
    {
        return getFactory(defaultFactory);
    }
    
    public static <T extends AlgorithmParserFactory> T getFactory(Class<T> factoryType) throws AlgorithmParserException
    {
        if(!factories.containsKey(factoryType))
            throw new AlgorithmParserException("The type '" + factoryType.getName() + "' has not been registered");
        
        return (T)factories.get(factoryType);
    }
    
    public static AlgorithmParserModuleInfo getModuleInfo(Class factoryType) throws AlgorithmParserException
    {
        if(!moduleInfos.containsKey(factoryType))
            throw new AlgorithmParserException("The type '" + factoryType.getName() + "' has not been registered");
        
        return moduleInfos.get(factoryType);
    }
    
    public static Class[] getFactoryTypes() throws AlgorithmParserException
    {       
        return factories.keySet().toArray(new Class[0]);
    }

    private Map<String, T> namedParsers;
    
    public AlgorithmParserFactory()
    {
        this.namedParsers = new HashMap<>();
    }
    
    public T getParser(String name) throws AlgorithmParserException
    {
        if(namedParsers.containsKey(name))
            return namedParsers.get(name);
        
        T instance = createParser();        
        namedParsers.put(name, instance);
        
        return instance;
    }
    
    public abstract T createParser() throws AlgorithmParserException;
}


package hmod.parser;

import hmod.core.Algorithm;

/**
 * Defines a parser to generate concrete algorithm objects. The parser should
 * sequentially read definitions, which can be retrieved from different sources
 * (files, net, etc) and the data provided by them can be linked through the
 * internal mechanisms of the parser. At some stage of the parsing process, an
 * algorithm object can be assembled and obtained in the form of an executable
 * object.
 * 
 * This class also serves as a factory of parser objects. Both cached handler
 * instances and independent handler instances can be generated through the 
 * {@link #getDefaultFactory(java.lang.String)} and the {@link #getDefaultFactory()} methods
 * respectively. The first creation mode is useful for modules that want to 
 * share the same handler instances. The creation of parsers depends directly on
 * the configured {@link AlgorithmParserFactory} type, which must be configured 
 * in the '<i>algorithm.parser.factory</i>' entry of a '<i>hmod.properties</i>' 
 * file, in the application folder.
 * 
 * @author Enrique Urra C.
 */
public interface AlgorithmParser
{
    /*private static String defaultModule = "hmod.builder";
    private static Map<String, AlgorithmParserModuleInfo> parserModules;
    private static Map<String, AlgorithmParserFactory> factories;
    private static Map<String, AlgorithmParser> namedParsers = new HashMap<>();*/
    
    /**
     * Inits the internal factory instance.
     * @throws AlgorithmParserException if an error occurs in the initialization.
     */
    /*private static void initParserModules() throws AlgorithmParserException
    {
        if(parserModules == null)
        {
            parserModules = new HashMap<>();
            factories = new HashMap<>();
            
            // Here, the parser modules are loaded
            Package[] pkgs = Package.getPackages();
            
            for(int i = 0; i < pkgs.length; i++)
            {
                String name;
                
                if((name = pkgs[i].getName()).startsWith("hmod.parser") && name.split(".").length == 3 && pkgs[i].isAnnotationPresent(AlgorithmParserModuleInfo.class))
                {
                    AlgorithmParserModuleInfo info = null;
                    AlgorithmParserFactory factory = null;
                    
                    try
                    {
                        info = pkgs[i].getAnnotation(AlgorithmParserModuleInfo.class);
                        factory = (AlgorithmParserFactory)ReflectionTool.createObject(info.factoryType());
                    }
                    catch(ReflectionException ex)
                    {
                        throw new AlgorithmParserException("Error while initializing the '" + name + "' module: " + ex.getLocalizedMessage(), ex);
                    }
                    
                    parserModules.put(name, info);
                    factories.put(name, factory);
                }
            }
            
            try
            {
                Properties prop = new Properties();
                prop.load(new FileInputStream("hmod.properties"));
                String defaultType = prop.getProperty("algorithm.parser.factory");
                
                parserModules = (AlgorithmParserFactory)ReflectionTool.createObject(defaultType);
            }
            catch(IOException ex)
            {
                throw new AlgorithmParserException("Cannot load the configuration file.", ex);
            }
            catch(ClassCastException ex)
            {
                throw new AlgorithmParserException("The configured default class is not a build manager", ex);
            }
            catch(ReflectionException ex)
            {
                throw new AlgorithmParserException(ex);
            }
        }
        
        if(parserModules.isEmpty())
            throw new AlgorithmParserException("No parser module has been loaded in the classpath");
    }*/
    
    /**
     * Creates a new instance of the default parser type configured.
     * @return The parser instance.
     * @throws AlgorithmParserException if an error occurs in the creation.
     */
    /*public static AlgorithmParser newInstance() throws AlgorithmParserException
    {
        return newInstance(defaultModule);
    }
    
    public static AlgorithmParser newInstance(String module) throws AlgorithmParserException
    {
        initParserModules();        
        
        if(!factories.containsKey(module))
            throw new AlgorithmParserException("The module '" + module + "' has not been loaded");
        
        return factories.get(module).createParser();
    }*/
    
    /**
     * Gets or create a named instance of the default handler type configured.
     * If the {@code name} parameter has been used previously to obtain a 
     * handler instance, it will be returned, otherwise a new instance will be
     * created.
     * @param name The identifier of the handler.
     * @return The handler instance.
     * @throws AlgorithmParserException if an error occurs in the creation.
     */
    /*public static AlgorithmParser getInstance(String name) throws AlgorithmParserException
    {
        return getInstance(name, defaultModule);
    }
    
    public static AlgorithmParser getInstance(String name, String module) throws AlgorithmParserException
    {
        initParserModules(); 
        
        if(namedParsers.containsKey(name))
            return namedParsers.get(name);
        
        AlgorithmParser instance = newInstance();        
        namedParsers.put(name, instance);
        
        return instance;
    }*/
    
    /**
     * Load an algorithm definition from some input (filesystem, packages, 
     * etc).
     * @param input A string with the input description.
     * @throws AlgorithmParserException if an error occurs in the parsing.
     */
    void load(String input, Object... args) throws AlgorithmParserException;
    
    /**
     * Resets the current parsing process.
     */
    void restart();
    
    /**
     * Gets the algorithm currently parsed through its first step.
     * @return The generated algorithm object.
     * @throws AlgorithmParserException if the state of the parsing process is 
     *  invalid.
     */
    Algorithm getAlgorithm() throws AlgorithmParserException;
}

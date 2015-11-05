
package hmod.parser.builders;

import flexbuilders.tree.TreeBuilder;
import static flexbuilders.tree.TreeBuilders.*;
import flexbuilders.core.BuildErrorType;
import flexbuilders.core.BuildException;
import flexbuilders.core.Builder;
import flexbuilders.scripting.BuildScript;
import flexbuilders.scripting.BuildScriptLibrary;
import flexbuilders.scripting.ScriptLibraryException;
import hmod.core.Algorithm;
import hmod.core.Step;
import hmod.parser.AlgorithmParser;
import hmod.parser.AlgorithmParserException;
import static hmod.parser.builders.AlgorithmBuilders.*;
import hmod.core.Config;
import java.net.URLClassLoader;

/**
 * Implementation of the {@link AlgorithmParser} component, which manages an 
 * internal {@link BuildHandler} instance to construct objects.<p/>
 * 
 * The {@link #getAlgorithm(java.lang.String)} method uses the internal handler 
 * instance to obtain a {@link Step} ref, which should be builded before  
 * throughthe input definitions.
 * 
 * This implementation forces a restart when:<p>
 * 
 * - The {@link BuildHandler#end(java.lang.String)} method 
 *   (which is called to createBuilder the starting {@link Step} instance) throws 
 *   a fatal createBuilder error. As such error should indicate an inconsistency of
 *   the createBuilder state, the parsing is forced to restart.<br>
 * - The algorithm object is successful created. This is done for avoiding
 *   incosistencies on further successful calls if the client modifies the
 *   generated algorithm instance.<p>
 * 
 * Both situations are informed to the client when a {@link AlgorithmParserException}
 * is thrown. Also, child classes may implement new scenarios when a restart is
 * required through the {@link #setRestartRequired(boolean)} and {@link #checkRestartRequired()}
 * methods.
 * 
 * This implementation processes particular inputs named "algorithm createBuilder 
 * scripts", which are classes that implement the {@link BuildScript} 
 * interface. Each time the {@link #load(java.lang.String)} method is 
 * called, the provided input should be a package path that leads to one of such
 * scripts. The class related to the script is loaded and its 
 * {@link BuildScript#process(hMod.parser.algorithmBuild.OLD_AlgorithmBuilder)}
 * method is inmediatelly called. The content of this method should define
 * building constructs enabled by different {@link Builder}  
 * implementations. Particularly, the {@link OLD_AlgorithmBuilder} implementation provides
 * specialized semantics for the construction of an algorithm flow diagram and
 * is used at the start of the script execution process.<p>
 * 
 * To load the classes related to the processed scripts, this implementation
 * creates a new class loader when the instance is initialized or restarted. 
 * Such class loader is a common {@link URLClassLoader} instance. This is 
 * important because the class loader is completely disposed when the parser is 
 * restarted, therefore, <b>only if the jar files from which the scripts classes
 * are loaded are not directly included in the application classpath</b>, they 
 * can be unloaded with each restart of the parser. This allows to perform 
 * modifications at runtime to the script and all their related classes code 
 * (which can be algorithm components in the same jar file). This is 
 * particularly useful when the scripts are being developed, and different 
 * building errors can occur and must be fixed without restarting the 
 * application.<p>
 * 
 * When this implementation is initialized, it reads from the '{@code hmod.properties}'
 * file the '{@code definitions.paths}' entry, which should contain different filesystem
 * paths to the jar files that contain the scripts to process. Such paths are 
 * used to initialize the {@link URLClassLoader} instances that are created on 
 * each parser restart. Multiple jar files can be specified (through both 
 * relative and absolute paths) in this entry by separating them with a 
 * semicolon ({@code ;}). As mentioned above, it is highly recommended to no 
 * include such jars in the application classpath if their classes are related 
 * to algorithm components and scripts that are at the development stage, for 
 * allowing their modifications at runtime. Components and scripts at the 
 * production stage may be in the classpath, as the ones provided in the 
 * <i>hMod</i> core.<p>
 * 
 * From the algorithm building perspective, the most useful feature of this
 * implementation is the leverage of the referencing system enabled by the
 * {@link ObjectTreeBuilder} component, which is used as the core builder in the
 * default implementation of the internal {@link BuildHandler} component.
 * When using this referencing system alongside the parse of multiple algorithm 
 * scripts, different algorithm components can be connected and assembled, 
 * although they may be defined in different scripts. This promotes the 
 * <b>modularization</b> of algorithms "parts" through different script files. 
 * For example, one could implement the main core of an algortithm in a single
 * script, while different specialized operators may be implemented in 
 * differents scripts, and both the core and the operators are connected only
 * by references. Then, the sequential parse of such scripts by using this class
 * performs the algorithm construction. Moreover, different operator 
 * implementations may be defined in different scripts, and depending on the
 * operator script that is parsed in this class is the operator implementation
 * that will be used in the final algorithm object.<p>
 * 
 * Consider the following example regarding the above information: a Genetic
 * Algorithm (GA) will be constructed through this component, and the following 
 * scripts are defined regarding such implementation in the '{@code example.ga.scripts}'
 * package:<p>
 * 
 * - {@code example.ga.scripts.Core}: The main GA core. Within the constructs
 *   in this file, two sub steps (through the 
 *   {@link OLD_AlgorithmBuilder#addSubStep(java.lang.String, java.lang.String, java.lang.String)}
 *   method) are defined pointing to '{@code crossoverOp}' and '{@code mutationOp}',
 *   which should be the initial steps of the crossover and mutation operators
 *   respectively. Also, the initial {@link Step} configured in this file has 
 *   the '{@code ga_start}' id.<br> 
 * - {@code example.ga.scripts.ops.crossover.OnePoint}: An implementation of the
 *   one-point crossover. Its first step has the '{@code crossoverOp}' id.<br>
 * - {@code example.ga.scripts.ops.crossover.TwoPoint}: An implementation of the
 *   two-point crossover. Its first step has the '{@code crossoverOp}' id.<br>
 * - {@code example.ga.scripts.ops.mutation.Bit}: An implementation of the
 *   single bit mutation. Its first step has the '{@code mutationOp}' id.<br>
 * - {@code example.ga.scripts.ops.mutation.Uniform}: An implementation of the
 *   uniform mutation. Its first step has the '{@code mutationOp}' id.<p>
 * 
 * One could createBuilder and run a simple instance of a GA as defined above through
 * the following code (asuming the jar that contains the scripts have been added
 * to the '{@code definitions.paths}' entry, and this implementation was configured in
 * the '<i>algorithm.parser.factory</i>' entry):
 * <pre>{@code
 *AlgorithmParser parser = AlgorithmParser.newInstance();
 *parser.load("example.ga.scripts.Core");
 *parser.load("example.ga.scripts.ops.crossover.OnePoint");
 *parser.load("example.ga.scripts.ops.mutation.Uniform");
 *Algorithm alg = parser.getAlgorithm("ga_start");
 *alg.execute();
 * }</pre> 
 * 
 * If one wants to change the mutation operator used, only the related class
 * name must be changed in the above code:
 * <pre>{@code
 *parser.load("example.ga.scripts.Core");
 *parser.load("example.ga.scripts.ops.crossover.OnePoint");
 *parser.load("example.ga.scripts.ops.mutation.Bit");
 *Algorithm alg2 = parser.getAlgorithm("ga_start");
 *alg2.execute();
 * }</pre>
 * 
 * Even the parsing order can be changed, because the referencing system 
 * supports a non-ordered building of referenced ids:
 * <pre>{@code
 *parser.load("example.ga.scripts.ops.mutation.Bit");
 *parser.load("example.ga.scripts.ops.crossover.OnePoint");
 *parser.load("example.ga.scripts.Core");
 *Algorithm alg3 = parser.getAlgorithm("ga_start");
 *alg3.execute();
 * }</pre>
 * 
 * Finally, it is important to remark that this implementation forces a restart
 * of the parser on the following events:<p>
 * 
 * - When a fatal createBuilder exception is thrown on both the scripts parsing (the
 *   {@link #load(java.lang.String)} method) or the algorithm object 
 *   generation (the {@link #getAlgorithm(java.lang.String)} method).<br>
 * - When the algorithm object is successful generated through the 
 *   {@link #getAlgorithm(java.lang.String)} method.<p>
 * 
 * Thus, the {@link #restart()} method should be called after the events 
 * described above, to allow any further parser usage.
 * 
 * @author Enrique Urra C.
 */
public final class TreeBuilderParser implements AlgorithmParser
{
    private static final String SCRIPTS_ENTRY = "hmod.parser.builder.scripts.paths";
    private TreeBuilder<Algorithm> currBuilder;
    private boolean restartRequired;
    private String[] paths;
    private BuildScriptLibrary currLibrary;

    TreeBuilderParser() throws BuildException
    {       
        restart();
        //String currFile = "";
        
        //try
        //{
            Config config = Config.getInstance();
            String scriptsPaths = config.getEntry(SCRIPTS_ENTRY);
            
            if(scriptsPaths == null)
                throw new BuildException("The scripts paths' entry '" + scriptsPaths + "' was not found in the hMod configuration");
            
            this.paths = scriptsPaths.split(";");
            
            /*File file = new File(scriptsPaths);
            
            if(!file.exists() || !file.isDirectory())
                throw new BuildException("The script-folder in the configuration file does not exists");
            
            String[] scriptBundles = file.list(new JarFilter());
            urls = new URL[scriptBundles.length];
            
            for(int i = 0; i < urls.length; i++)
            {
                currFile = scriptsPaths + "/" + scriptBundles[i];
                urls[i] = new File(currFile).toURI().toURL();
            }*/
        /*}
        catch(MalformedURLException ex)
        {
            throw new BuildException("The '" + currFile  +"' script bundle invalid.", ex);
        }*/
    }
    
    /**
     * Inits the internal classloader, if it has not been initialized yet.
     */
    private void initLibrary() throws AlgorithmParserException
    {
        if(currLibrary == null)
        {
            try
            {
                currLibrary = BuildScriptLibrary.getFromPaths(paths);
            }
            catch(ScriptLibraryException ex)
            {
                throw new AlgorithmParserException("Cannot initialize the scripts' library", ex);
            }
        }
    }
    
    private void checkRestartRequired() throws AlgorithmParserException
    {
        if(restartRequired)
            throw new AlgorithmParserException(true, "The parser must be restarted before calling this operation");
    }
    
    /**
     * Parses an algortithm createBuilder script that is implemented in the class 
     * specified in the {@code input} argument, as a package path. Such class
     * must implement the {@link BuildScript} interface.<p>
     * 
     * This implementation loads the input class through the internal class 
     * loader, tries to instantiate it using a non-argument constructor, and 
     * executes its {@link BuildScript#process(hMod.parser.algorithmBuild.OLD_AlgorithmBuilder)}
     * method to begin the parsing.<p>
     * 
     * If a createBuilder exception is thrown while the script is being executed, this 
     * method will force a restart the parser (through the {@link TreeBuilderParser#setRestartRequired(boolean)} method)
     * to avoid any createBuilder state inconsistency regarding the previous inputs 
     * loaded.
     */
    @Override
    public void load(String input, Object... args) throws AlgorithmParserException
    {
        checkRestartRequired();
        initLibrary();
        
        try
        {
            BuildScript script = currLibrary.createScript(input, currBuilder, args);
            script.process();
        }
        catch(ScriptLibraryException | BuildException ex)
        {
            restartRequired = true;
            throw new AlgorithmParserException(true, "The script '" + input + "' cannot be loaded (" + ex.getMessage() + ")", ex);
        }
    }

    @Override
    public void restart()
    {
        try
        {
            currBuilder = tree();
            currBuilder.root().setBuildable(algorithm()).readOnly();
            
            restartRequired = false;
            //loader = null;
            currLibrary = null;
        }
        catch(BuildException ex)
        {
            throw new RuntimeException("Cannot create a builder", ex);
        }
    }
    
    /**
     * Gets the algortithm instance by specifying the id of the first {@link Step}
     * object of the algorithm construct. This implementation enables the force 
     * restart state when a fatal createBuilder error is generated, or the algortihm is
     * successful generated.
     */
    @Override
    public Algorithm getAlgorithm() throws AlgorithmParserException
    {
        checkRestartRequired(); 
        
        try
        {
            Algorithm created = currBuilder.build();            
            restartRequired = true;
            
            return created;
        }
        catch(BuildException ex)
        {            
            if(ex.getSeverity() == BuildErrorType.FATAL)
                restartRequired = true;
            
            throw new AlgorithmParserException(restartRequired, ex.getLocalizedMessage(), ex);
        }
    }
}
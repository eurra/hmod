
package hmod.domains.launcher.graph;

import hmod.domains.launcher.core.AlgorithmLoadException;
import hmod.domains.launcher.core.AlgorithmLoader;
import flexbuilders.core.BuildException;
import java.util.HashSet;
import optefx.util.bundlelib.BundleLibrary;
import optefx.util.bundlelib.BundleException;
import optefx.util.tools.ReflectionException;
import optefx.util.tools.ReflectionTools;

/**
 * 
 * @author Enrique Urra C.
 */
public final class AlgorithmScriptLoader implements AlgorithmLoader
{
    public static final String ENTRY_SCRIPTS_PATH = "hmod.loader.graph.scriptsPaths";
            
    private final HashSet<AlgorithmLayer> layers;
    private boolean restartRequired;
    private BundleLibrary library;
    private final String[] paths;
    private NodeIdFinder finder;

    public AlgorithmScriptLoader()
    {
        this(new String[]{});
    }
    
    public AlgorithmScriptLoader(String... paths)
    {
        this.paths = new String[paths.length];
        
        for(int i = 0; i < paths.length; i++)
        {
            if(paths[i] == null)
                throw new NullPointerException("Null path at position " + i);
            
            this.paths[i] = paths[i];
        }
        
        layers = new HashSet<>();
        restart();
    }
    
    private BundleLibrary getLibrary() throws AlgorithmLoadException
    {
        if(library == null)
        {
            try
            {
                library = BundleLibrary.getFromPaths(paths);
            }
            catch(BundleException ex)
            {
                throw new AlgorithmLoadException("Cannot initialize the scripts' library", ex);
            }
        }
        
        return library;
    }
    
    private NodeIdFinder getNodeIdFinder()
    {
        if(finder == null)
        {
            BundleLibrary lib = getLibrary();
            finder = new NodeIdFinder(lib.getLoader());
        }
        
        return finder;
    }
    
    private void checkRestartRequired() throws AlgorithmLoadException
    {
        if(restartRequired)
            throw new AlgorithmLoadException(true, "The parser must be restarted before calling this operation");
    }
    
    public AlgorithmLayer createLayer()
    {
        checkRestartRequired();
        
        AlgorithmLayer layer = new AlgorithmLayer(getNodeIdFinder());
        layers.add(layer);
        
        return layer;
    }
    
    @Override
    public AlgorithmLoader load(String input, Object... args) throws AlgorithmLoadException
    {
        checkRestartRequired();
        Class scriptClass;
        
        try
        {
            scriptClass = getLibrary().loadClass(input);
        }
        catch(BundleException ex)
        {
            restartRequired = true;
            throw new AlgorithmLoadException(true, "The script '" + input + "' cannot be loaded (" + ex.getMessage() + ")", ex);
        }
        
        if(layers == null)
            throw new NullPointerException("Null layers array");
        
        Object[] totalArgs = new Object[args.length];
        int i;
        
        for(i = 0; i < args.length; i++)
        {
            if(args[i] == null)
                throw new NullPointerException("Null argument at position " + i);
            
            if(args[i] instanceof AlgorithmLayer && !this.layers.contains((AlgorithmLayer)args[i]))
                throw new AlgorithmLoadException("The layer at position " + i + " do not belongs to this loader");
            
            totalArgs[i] = args[i];
        }
        
        AlgorithmScript resultScript = null; 
        
        try
        {
            resultScript = (AlgorithmScript)ReflectionTools.createObject(scriptClass, totalArgs);
            resultScript.setContextClassLoader(getLibrary().getLoader());
        }
        catch(ReflectionException ex)
        {
            String exMsg = "Cannot create a script of type '" + scriptClass.getName() + "'";
        
            if(args.length > 0)
            {
                exMsg += " with the following arguments: ";
                
                for(int j = 0; j < args.length; j++)
                    exMsg += (args[j] == null ? "null" : args[j].getClass() + (j <= args.length - 1 ? ", " : ""));
            }
            else
            {
                exMsg += " with no arguments.";
            }

            throw new AlgorithmLoadException(exMsg + " (" + ex.getLocalizedMessage() + ")", ex);
        }
        catch(ClassCastException ex)
        {
            throw new AlgorithmLoadException("The type '" + scriptClass + "' is not compatible with '" + AlgorithmScript.class + "'", ex);
        }
        
        try
        {
            resultScript.process();
            return this;
        }
        catch(BuildException ex)
        {
            restartRequired = true;
            throw new AlgorithmLoadException(true, "Error when executing the script '" + input + "' (" + ex.getMessage() + ")", ex);
        }
    }
    
    public ClassLoader getClassLoader()
    {
        checkRestartRequired();
        
        return getLibrary().getLoader();
    }

    @Override
    public void restart()
    {
        for(AlgorithmLayer layer : layers)
            layer.dispose(); 
        
        layers.clear();
        restartRequired = false;
        library = null;
        finder = null;
    }
}

package hmod.launcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.function.BiFunction;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

/**
 *
 * @author Enrique Urra C.
 */
public final class ScriptLauncher
{
    private static final NashornScriptEngineFactory factory;
    
    static
    {
        ScriptEngineManager manager = new ScriptEngineManager();
        List<ScriptEngineFactory> factories = manager.getEngineFactories();
        NashornScriptEngineFactory found = null;
        
        for(ScriptEngineFactory toCheckFactory : factories)
        {
            if(toCheckFactory instanceof NashornScriptEngineFactory)
            {
                found = (NashornScriptEngineFactory)toCheckFactory;
                break;
            }
        }
        
        if(found == null)
            throw new RuntimeException("nashorn engine not found");

        factory = found;
    }
    
    private ScriptEngine engine;
    private final BundleLoader bundleHandler;
    private final LauncherControl launcherHandler;
    private final CommandParser commandParseHandler;
    private final VariableRegistry variableHandler;

    ScriptLauncher(BundleLoader bundleHandler,
                   LauncherControl launcherHandler,
                   CommandParser commandParseHandler,
                   VariableRegistry variableHandler)
    {
        this.bundleHandler = bundleHandler;
        this.launcherHandler = launcherHandler;
        this.commandParseHandler = commandParseHandler;
        this.variableHandler = variableHandler;
    }
    
    private void checkRequireInit()
    {
        if(engine == null)
            restart();
    }

    public void eval(Reader inputFile) throws ScriptException
    {
        eval(inputFile, null);
    }
    
    public void eval(Reader inputFile, String fileName) throws ScriptException
    {
        checkRequireInit();
        
        if(fileName != null)
            engine.put(ScriptEngine.FILENAME, fileName);
        
        engine.eval(inputFile);
    }

    public void eval(String input) throws ScriptException
    {
        checkRequireInit();
        engine.eval(input);
    }

    public Object getVariable(String name)
    {
        checkRequireInit();
        return engine.getBindings(ScriptContext.ENGINE_SCOPE).get(name);
    }

    public void setVariable(String name, Object val)
    {
        checkRequireInit();
        engine.getBindings(ScriptContext.ENGINE_SCOPE).put(name, val);
    }
    
    public final void restart()
    {
        engine = factory.getScriptEngine(bundleHandler.getLoader());
        engine.setBindings(variableHandler.asBindings(), ScriptContext.GLOBAL_SCOPE);
                
        try
        {
            BiFunction<String, Object[], CommandRunner> handler = commandParseHandler::parseCommand;
            engine.eval(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/hmod/launcher/launcher-core.js"))));
            ScriptObjectMirror commandHandlerObj = (ScriptObjectMirror)engine.getBindings(ScriptContext.ENGINE_SCOPE).get("console");
            commandHandlerObj.put("handler", handler);
        }
        catch(ScriptException ex)
        {
            throw new LauncherException("Cannot initialize script engine: ", ex);
        }        
        
        File[] autoexecPaths = new File(launcherHandler.getScriptBaseFolder()).listFiles((pathname) ->
        {
            if(!pathname.isDirectory())
                return false;

            if(pathname.getName().equalsIgnoreCase("autoexec"))
                return true;
            
            return false;
        });
        
        if(autoexecPaths != null && autoexecPaths.length > 0)
        {
            File[] allJSFiles = autoexecPaths[0].listFiles((pathname) -> 
            {
                if(pathname.isFile() && pathname.getName().endsWith("js"))
                    return true;

                return false;
            });
            
            for(int i = 0; i < allJSFiles.length; i++)
            {
                try
                {
                    engine.eval(new FileReader(allJSFiles[i]));
                }
                catch(FileNotFoundException | ScriptException ex)
                {
                    throw new LauncherException("Cannot init the script autoexec file: " + allJSFiles[i], ex);
                }
            }
        }
    }
}

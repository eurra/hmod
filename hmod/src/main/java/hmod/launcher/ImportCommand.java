
package hmod.launcher;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.script.ScriptException;
import optefx.util.bundlelib.BundleException;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="import",
    usage="import <types and/or methods>",
    description="Loads a set of classes or a static methods into the script environment.\n"
        + "<types and/or methods>: A set of elements to import (autodetected)."
)
class ImportCommand extends Command
{
    private BundleHandler bundleHandler;
    private ScriptEngineHandler scriptEngineHandler;

    public void setBundleHandler(BundleHandler bundleHandler)
    {
        this.bundleHandler = bundleHandler;
    }

    public void setScriptEngineHandler(ScriptEngineHandler scriptEngineHandler)
    {
        this.scriptEngineHandler = scriptEngineHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
            throw new CommandUsageException(this);
        
        int count = args.getCount();
        String toEval = "";
        
        for(int i = 0; i < count; i++)
        {
            String input = args.getString(i);
            String[] splited = input.split("\\.");
            boolean isValid = false;
            
            // Check if input is a class
            try
            {
                bundleHandler.loadClass(input);
                toEval += "var " + splited[splited.length - 1] + " = Java.type('" + input + "');";
                isValid = true;
            }
            catch(BundleException ex)
            {
                // Check if input is a static method
                if(splited.length >= 2)
                {
                    String classPrefix = input.substring(0, input.lastIndexOf("."));
                    String methodName = input.substring(input.lastIndexOf(".") + 1, input.length());
                    
                    try
                    {
                        Class clazz = bundleHandler.loadClass(classPrefix);
                        
                        boolean found = false;
                        Method[] methods = clazz.getMethods();

                        for(int j = 0; j < methods.length && !found; j++)
                        {
                            if(Modifier.isStatic(methods[j].getModifiers()) && methods[j].getName().equals(methodName))
                                found = true;
                        }

                        if(found)
                        {
                            toEval += "var " + splited[splited.length - 2] + " = Java.type('" + classPrefix + "'); var " + methodName + " = " + splited[splited.length - 2] + "." + methodName + ";";
                            isValid = true;
                        }
                    }
                    catch(BundleException ex2)
                    {
                    }
                }
            }
            
            if(!isValid)
                throw new LauncherException("No class or static method '" + input + "' was found");
        }
        
        try
        {
            scriptEngineHandler.eval(toEval);
        }
        catch(ScriptException ex)
        {
            throw new LauncherException("Cannot configure the import variables: " + ex.getLocalizedMessage(), ex);
        }
        
        OutputManager.println(Launcher.OUT_COMMON, count + " type(s) and/or method(s) imported.");
    }
}

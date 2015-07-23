
package hmod.launcher;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.Bindings;
import javax.script.SimpleBindings;

/**
 *
 * @author Enrique Urra C.
 */
public final class VariableHandler
{
    /**
     * The table that store the custom variables.
     */
    private final Map<String, Object> variableMap = new HashMap<>();

    VariableHandler()
    {
    }
    
    public Bindings asBindings()
    {
        return new SimpleBindings(variableMap);
    }

    /**
     * Sets a custom variable value.
     * @param name The name of the variable.
     * @param val The value of the variable.
     */
    public void setVariable(String name, Object val)
    {
        if(name == null)
            throw new NullPointerException("Null variable name");
        
        if(val == null)
            throw new NullPointerException("Null variable value");
        
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(name);

        if(matcher.find())
            throw new LauncherException("The variable name cannot contain spaces (" + name + ")");
        
        variableMap.put(name, val);
    }

    public <T> Map<String, T> getSubmapOfTypes(Class<T> type)
    {
        Map<String, T> submap = new HashMap<>(variableMap.size());
        
        for(String key : variableMap.keySet())
        {
            Object val = variableMap.get(key);
            
            if(type.isAssignableFrom(val.getClass()))
                submap.put(key, type.cast(val));
        }
        
        return submap;
    }

    public String[] getAllVariables()
    {
        return variableMap.keySet().toArray(new String[0]);
    }

    /**
     * Gets the value of a particular variable.
     * @param name The name of the variable.
     * @return The value, or null if the variable has not been set.
     */
    public Object getValue(String name)
    {
        return variableMap.get(name);
    }

    public <T> T getValueOfType(String name, Class<T> type)
    {
        Object check = getValue(name);
        
        if(check == null || type.isAssignableFrom(check.getClass()))
            return null;
        
        return type.cast(check);
    }
    
    public boolean isSet(String name)
    {
        return variableMap.containsKey(name);
    }
    
    /**
     * Clears all custom variables added.
     */
    public void clearAll()
    {
        variableMap.clear();
    }
}

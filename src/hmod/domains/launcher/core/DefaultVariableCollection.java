
package hmod.domains.launcher.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Enrique Urra C.
 */
public final class DefaultVariableCollection implements VariableCollection
{
    /**
     * The table that store the custom variables.
     */
    private final Map<String, Object> variableMap;
    private final VariableCollection parent;

    public DefaultVariableCollection()
    {
        this(null);
    }
    
    public DefaultVariableCollection(VariableCollection parent)
    {
        this.variableMap = new HashMap<>();
        this.parent = parent;
    }

    /**
     * Sets a custom variable value.
     * @param name The name of the variable.
     * @param val The value of the variable.
     */
    @Override
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

    @Override
    public <T> Map<String, T> getSubmapOfTypes(Class<T> type)
    {
        Map<String, T> submap;
        
        if(parent != null)
        {
            Map<String, T> parentSubMap = parent.getSubmapOfTypes(type);
            submap = new HashMap<>(parentSubMap);
        }
        else
        {
            submap = new HashMap<>(variableMap.size());
        }
        
        for(String key : variableMap.keySet())
        {
            Object val = variableMap.get(key);
            
            if(type.isAssignableFrom(val.getClass()))
                submap.put(key, type.cast(val));
        }
        
        return submap;
    }

    @Override
    public String[] getAllVariables()
    {
        if(parent != null)
        {
            String[] parentVars = parent.getAllVariables();
            HashSet<String> set = new HashSet<>(Arrays.asList(parentVars));
            Set<String> currVars = variableMap.keySet();
            
            for(String var : currVars)
                set.add(var);
            
            return set.toArray(new String[0]);
        }
        else
        {
            return variableMap.keySet().toArray(new String[0]);
        }
    }

    /**
     * Gets the value of a particular variable.
     * @param name The name of the variable.
     * @return The value, or null if the variable has not been set.
     */
    @Override
    public Object getValue(String name)
    {
        Object val = variableMap.get(name);
        
        if(val == null && parent != null)
            val = parent.getValue(name);
        
        return val;
    }

    @Override
    public <T> T getValueOfType(String name, Class<T> type)
    {
        Object check = getValue(name);
        
        if(check == null || type.isAssignableFrom(check.getClass()))
            return null;
        
        return type.cast(check);
    }
    
    @Override
    public boolean isSet(String name)
    {
        return variableMap.containsKey(name) || (parent != null && parent.isSet(name));
    }
    
    /**
     * Clears all custom variables added.
     */
    @Override
    public void clearAll()
    {
        variableMap.clear();
    }
}

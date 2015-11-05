
package hmod.launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a component for variable management.
 * @author Enrique Urra C.
 */
public final class VariableManager
{
    /**
     * The internal variable processor list.
     */
    private List<VariableProcessor> processorList;
    /**
     * The table that store the custom variables.
     */
    private Map<String, String> variableMap;
    /**
     * The string used as variable delimiter.
     */
    private String delimiter;

    /**
     * Constructor.
     * @param delimiter The String to be used as delimiter.
     */
    public VariableManager(String delimiter)
    {
        if(delimiter == null || delimiter.isEmpty())
            throw new IllegalArgumentException("the provided delimiter cannot be null or empty.");
        
        this.delimiter = delimiter;
        this.processorList = new ArrayList<>();
        this.variableMap = new HashMap<>();
    }
    
    /**
     * Adds a new variable processor to the manager.
     * @param processor The processor object.
     */
    public void addProcessor(VariableProcessor processor) throws LauncherException
    {
        if(processor == null)
            throw new LauncherException("The provided processor cannot be null.");
        
        if(!processorList.contains(processor))
            processorList.add(processor);
    }
    
    /**
     * Tries to replace all pattern findings according to the current added
     * processors and custom variables into an input string.
     * @param input The input to process.
     * @return The processed input, with the variables processed.
     * @throws LauncherException if an error occurs during a replacement.
     */
    public String tryReplace(String input) throws LauncherException
    {
        if(processorList.isEmpty())
            return input;
        
        //Pattern regex = Pattern.compile("(?<=\\" + delimiter + ")(.*?)(?=\\" + delimiter + ")");
        Pattern regex = Pattern.compile(delimiter + "(.*?)" + delimiter);
        Matcher regexMatcher = regex.matcher(input);
        HashSet<String> findings = new HashSet<>();
        
        while(regexMatcher.find())
        {
            String find = regexMatcher.group().replaceAll(delimiter, "");
            
            if(!findings.contains(find))
                findings.add(find);
        }
        
        String newInput = input;
        
        for(String find : findings)
        {
            String toModify = find;
            
            // The processors are executed at first
            for(VariableProcessor processor : processorList)
                toModify = processor.process(toModify);
            
            // The custom variables are evaluated after
            for(String varName : variableMap.keySet())
                toModify = toModify.replace(varName, variableMap.get(varName));
            
            // The result is finally replaced within the input if it changed
            if(!toModify.equals(find))
                newInput = newInput.replace(delimiter + find + delimiter, toModify);
        }
        
        return newInput;
    }
    
    /**
     * Sets a custom variable value.
     * @param name The name of the variable.
     * @param val The value of the variable.
     */
    public void setVariable(String name, String val)
    {
        variableMap.put(name, val);
    }
    
    /**
     * Clears all custom variables added.
     */
    public void clearVariables()
    {
        variableMap.clear();
    }
    
    /**
     * Gets an array with the defined variables.
     * @return The variables as an String array.
     */
    public String[] getVariables()
    {        
        return variableMap.keySet().toArray(new String[variableMap.size()]);
    }
    
    /**
     * Gets the value of a particular variable.
     * @param name The name of the variable.
     * @return The String value, or null if the variable has not been set.
     */
    public String getVariableValue(String name)
    {
        return variableMap.get(name);
    }
}

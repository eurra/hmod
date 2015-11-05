
package hmod.launcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements a parser for command entries.
 * @author Enrique Urra C.
 */
public class CommandParser
{
    /**
     * The register of allowed commands.
     */
    private CommandRegister register;
    /**
     * The variable manager used.
     */
    private VariableManager varManager;

    /**
     * Constructor.
     * @param register The command register to use.
     * @param varManager The variable manager to use.
     * @throws NullPointerException if the register or the manager are null.
     */
    public CommandParser(CommandRegister register, VariableManager varManager)
    {
        if(register == null)
            throw new NullPointerException("The provided register cannot be null");
        
        if(varManager == null)
            throw new NullPointerException("The provided variable manager cannot be null");
        
        this.varManager = varManager;
        this.register = register;
    }
    
    /**
     * Parses a command input.
     * @throws IOException if an error occurs in the parsing.
     * @throws UndefinedCommandException if the parsed command is not registered.
     */
    public CommandExecInfo parseInput(String input) throws LauncherException
    { 
        String trimInput = input.trim();
        
        if(trimInput.startsWith("%") || trimInput.isEmpty())
             return null;
        
        Pattern regexAfter = Pattern.compile("^(\\S+)\\s(.+)$");
        Matcher regexMatcher = regexAfter.matcher(trimInput);
        String argsInput;
        
        if(regexMatcher.find())
            argsInput = regexMatcher.group(2);
        else
            argsInput = null;
        
        Pattern regexBefore = Pattern.compile("([^\\s]+)");
        regexMatcher = regexBefore.matcher(trimInput);
        String word;
        
        if(regexMatcher.find())
            word = regexMatcher.group();
        else
            word = trimInput;
        
        return new CommandExecInfo(register.checkCommand(word), argsInput);
    }
    
    public String[] parseArguments(String arguments) throws LauncherException
    {
        if(arguments == null)
            return new String[0];
        
        arguments = varManager.tryReplace(arguments);
            
        Pattern regexArgs = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regexArgs.matcher(arguments);
        ArrayList<String> inputs = new ArrayList<>();

        while(regexMatcher.find())
            inputs.add(regexMatcher.group().replaceAll("\"", "").replaceAll("'", ""));

        return inputs.toArray(new String[inputs.size()]);
    }
}

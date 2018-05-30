
package hmod.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Enrique Urra C.
 */
public final class CommandParser
{
    private static class InnerCommandRunner implements CommandRunner
    {
        private final Command cmd;
        private final CommandArgs args;  

        public InnerCommandRunner(Command cmd, CommandArgs args)
        {
            this.cmd = cmd;
            this.args = args;
        }

        @Override
        public void runCommand() throws LauncherException
        {
            cmd.executeCommand(args);
        }
    }
    
    private static class CustomArgs implements CommandArgs
    {
        private final Object[] args;
        private final TextProcessorRegistry textProcessorHandler;

        public CustomArgs(Object[] args, TextProcessorRegistry textProcessorHandler)
        {
            this.args = args;
            this.textProcessorHandler = textProcessorHandler;
        }
        
        @Override
        public int getCount()
        {
            return args.length;
        }
        
        @Override
        public Object getObject(int index) throws IndexOutOfBoundsException
        {
            return getArgAs(index, Object.class);
        }

        @Override
        public String getString(int index) throws IndexOutOfBoundsException, LauncherException
        {
            return getObject(index).toString();
        }
        
        @Override
        public <T> T getArgAs(int index, Class<T> type) throws IndexOutOfBoundsException, LauncherException
        {
            if(index < 0 || index >= args.length)
                throw new IndexOutOfBoundsException("Wrong argument index: " + index);
            
            Object arg = args[index];
            Class argClass = arg.getClass();
            
            if(!type.isAssignableFrom(argClass))
                throw new LauncherException("The argument at index " + index + " is not compatible with the type " + type.getName());
            
            Object checkedString = null;
            
            if(arg instanceof String)
                checkedString = textProcessorHandler.parseText((String)arg);
            
            if(checkedString != null)
                arg = checkedString;
            
            return type.cast(arg);
        }

        @Override
        public Object[] getAllArgs()
        {
            return Arrays.copyOf(args, args.length);
        }
    }
    
    private final CommandRegistry cr;
    private final VariableRegistry variableHandler;
    private final TextProcessorRegistry textProcessorHandler;

    CommandParser(CommandRegistry commandHandler,
                  VariableRegistry variableHandler,
                  TextProcessorRegistry textProcessorHandler)
    {
        this.textProcessorHandler = textProcessorHandler;
        this.cr = commandHandler;
        this.variableHandler = variableHandler;
    }

    private Command checkCommand(String word)
    {
        Command cmd = cr.getCommand(word);
        
        if(cmd == null)
            throw new UndefinedCommandException(word);
        else if(!cmd.isEnabled())
            throw new UnavailableCommandException(word);
        
        return cmd;
    }
    
    private CommandArgs parseArguments(String args) throws LauncherException
    {
        Object[] parsedArgs;
        
        if(args == null)
        {
            parsedArgs = new Object[0];
        }
        else
        {
            Pattern regexArgs = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
            Matcher regexMatcher = regexArgs.matcher(args);
            ArrayList<Object> inputs = new ArrayList<>();

            while(regexMatcher.find())
            {
                String toCheck = regexMatcher.group();
                
                if((toCheck.startsWith("\"") && toCheck.endsWith("\"")) || (toCheck.startsWith("'") && toCheck.endsWith("'")))
                {
                    inputs.add(toCheck.replaceAll("\"", "").replaceAll("'", ""));
                }
                else
                {
                    if(variableHandler.isSet(toCheck))
                        inputs.add(variableHandler.getValue(toCheck));
                    else
                        inputs.add(toCheck);
                }
            }

            parsedArgs = inputs.toArray(new String[inputs.size()]);
        }
        
        return new CustomArgs(parsedArgs, textProcessorHandler);
    }
    
    public CommandRunner parseCommand(String input) throws LauncherException
    {
        String trimInput = input.trim();
        
        // Check word
        Pattern regexBefore = Pattern.compile("([^\\s]+)");
        Matcher regexMatcher = regexBefore.matcher(trimInput);
        String word;

        if(regexMatcher.find())
            word = regexMatcher.group();
        else
            word = trimInput;

        Command cmd = checkCommand(word);

        // Check args
        Pattern regexAfter = Pattern.compile("^(\\S+)\\s(.+)$");
        regexMatcher = regexAfter.matcher(trimInput);
        String argsInput;

        if(regexMatcher.find())
            argsInput = regexMatcher.group(2);
        else
            argsInput = null;

        CommandArgs args = parseArguments(argsInput);
        return new InnerCommandRunner(cmd, args);
    }

    public CommandRunner parseCommand(String cmd, Object[] args) throws LauncherException
    {
        if(args == null)
            throw new NullPointerException("Null args");
        
        Command checkedCmd = checkCommand(cmd);
        return new InnerCommandRunner(checkedCmd, new CustomArgs(args, textProcessorHandler));
    }
}

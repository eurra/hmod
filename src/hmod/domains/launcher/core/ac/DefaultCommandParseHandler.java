
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandRunner;
import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.TextVariableProcessor;
import hmod.domains.launcher.core.UnavailableCommandException;
import hmod.domains.launcher.core.UndefinedCommandException;
import hmod.domains.launcher.core.VariableCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultCommandParseHandler implements CommandParseHandler
{
    private static class InnerCommandRunner implements CommandRunner
    {
        private final Command cmd;
        private final String[] args;
        private final CommandProcessor processor;

        public InnerCommandRunner(Command cmd, String[] args, CommandProcessor processor)
        {
            this.cmd = cmd;
            this.args = args;
            this.processor = processor;
        }

        @Override
        public void runCommand() throws LauncherException
        {
            processor.runCommand(cmd, args);
        }
    }
    
    private static class CustomArgs implements CommandArgs
    {
        private final Object[] args;

        public CustomArgs(Object[] args)
        {
            this.args = args;
        }
        
        @Override
        public int getCount()
        {
            return args.length;
        }
        
        @Override
        public Object getObject(int index) throws IndexOutOfBoundsException
        {
            if(index < 0 || index >= args.length)
                return new IndexOutOfBoundsException("Wrong argument index: " + index);
            
            return args[index];
        }

        @Override
        public String getString(int index) throws IndexOutOfBoundsException, LauncherException
        {
            return getArgAs(index, String.class);
        }
        
        @Override
        public <T> T getArgAs(int index, Class<T> type) throws IndexOutOfBoundsException, LauncherException
        {
            Object arg = getObject(index);
            
            if(!type.isAssignableFrom(arg.getClass()))
                throw new LauncherException("The argument at index " + index + " is not compatible with the type " + type.getName());
            
            return type.cast(arg);
        }

        @Override
        public Object[] getAllArgs()
        {
            return Arrays.copyOf(args, args.length);
        }
    }
    
    private static class CommandProcessor
    {
        private List<TextVariableProcessor> processorList;
        private VariableHandler variableHandler;

        public CommandProcessor(TextVariableProcessor[] processors)
        {
            if (processors == null || processors.length == 0)
                throw new IllegalArgumentException("Null or empty processors array");
        
            this.processorList = new ArrayList<>(processors.length);

            for (int i = 0; i < processors.length; i++)
            {
                TextVariableProcessor processor = processors[i];

                if (processor == null)
                    throw new IllegalArgumentException("Null processor at position " + i);

                if (!processorList.contains(processor))
                    processorList.add(processor);
            }
        }
        
        public void runCommand(Command cmd, String[] args)
        {
            Object[] compiledArgs = new Object[args.length];
            
            for(int i = 0; i < args.length; i++)
                compiledArgs[i] = toObject(args[i]);
            
            cmd.executeCommand(new CustomArgs(compiledArgs));
            
        }
        
        private Object toObject(String input) throws LauncherException
        {
            if(input == null)
                throw new NullPointerException("Null input");
            
            if(variableHandler == null)
                throw new NullPointerException("Null variable handler");
            
            if(processorList.isEmpty() || input.length() == 1)
                return input;
            
            VariableCollection varCol= variableHandler.getActiveVariables();
            Pattern regex = Pattern.compile("\\{(.*?)\\}");
            Matcher regexMatcher = regex.matcher(input);
            ArrayList<String> findings = new ArrayList<>();

            while(regexMatcher.find())
            {
                String find = regexMatcher.group();

                if (!findings.contains(find))
                    findings.add(find);
            }
            
            if(findings.isEmpty())
            {
                try
                {
                    return NumberUtils.createNumber(input);
                }
                catch(NumberFormatException ex)
                {
                    return input;
                }
            }
            
            // If there is only one match and it corresponds to the complete
            // argument, there can be a variable that directly replaces it  
            String firstMatch = findings.get(0);
            
            if(findings.size() == 1 && firstMatch.equals(input))
            {
                String withoutBraces = firstMatch.substring(1, firstMatch.length() - 1);
                Object check = varCol.getValue(withoutBraces);

                if(check != null)
                    return check;
            }
            
            // Otherwise, the found matches are treated as string-variables
            String newInput = input;
            Map<String, String> stringVars = varCol.getSubmapOfTypes(String.class);
            
            for(String find : findings)
            {
                String withoutBraces = find.substring(1, find.length() - 1);
                String replacement = null;

                // Custom variables are evaluated first 
                replacement = stringVars.get(withoutBraces);

                // The text processors are executed if no custom variable 
                // replacement was found
                if(replacement == null)
                {
                    int processorCount = processorList.size();
                
                    for(int i = 0; i < processorCount && replacement == null; i++)
                        replacement = processorList.get(i).process(withoutBraces);
                }

                // If a replacement was found, the result is replaced within the 
                // input
                if(replacement != null)
                    newInput = newInput.replace(find, replacement);
            }

            return newInput;
        }
    }
    
    public static final String COMMENT_CHAR = "#";
    
    private CommandRunner currParsed;
    private CommandHandler commandHandler;
    private final CommandProcessor processor;

    public DefaultCommandParseHandler(TextVariableProcessor[] processors)
    {
        this.processor = new CommandProcessor(processors);
    }

    public void setCommandHandler(CommandHandler commandHandler)
    {
        this.commandHandler = commandHandler;
    }
    
    public void setVariableHandler(VariableHandler variableHandler)
    {
        this.processor.variableHandler = variableHandler;
    }

    @Override
    public CommandRunner parseNextCommand(String input) throws LauncherException
    {
        String trimInput = input.trim();

        if(trimInput.startsWith(COMMENT_CHAR) || trimInput.isEmpty())
        {
            currParsed = null;
            return null;
        }

        // Check word
        Pattern regexBefore = Pattern.compile("([^\\s]+)");
        Matcher regexMatcher = regexBefore.matcher(trimInput);
        String word;

        if(regexMatcher.find())
            word = regexMatcher.group();
        else
            word = trimInput;

        Command cmd = commandHandler.getCommand(word);
        
        if(cmd == null)
            throw new UndefinedCommandException(word);
        else if(!cmd.isEnabled())
            throw new UnavailableCommandException(word);

        // Check args
        Pattern regexAfter = Pattern.compile("^(\\S+)\\s(.+)$");
        regexMatcher = regexAfter.matcher(trimInput);
        String argsInput;

        if(regexMatcher.find())
            argsInput = regexMatcher.group(2);
        else
            argsInput = null;

        String[] args;

        if(argsInput == null)
        {
            args = new String[0];
        }
        else
        {
            Pattern regexArgs = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
            regexMatcher = regexArgs.matcher(argsInput);
            ArrayList<String> inputs = new ArrayList<>();

            while(regexMatcher.find())
                inputs.add(regexMatcher.group().replaceAll("\"", "").replaceAll("'", ""));

            args = inputs.toArray(new String[inputs.size()]);
        }

        // End
        currParsed = new InnerCommandRunner(cmd, args, processor);
        return currParsed;
    }

    @Override
    public CommandRunner getCommandCurrentlyParsed() throws DataHandlingException
    {
        return currParsed;
    }
}

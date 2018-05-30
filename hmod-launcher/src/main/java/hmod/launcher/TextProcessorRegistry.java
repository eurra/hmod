
package hmod.launcher;

import hmod.ap.FindOperations;
import hmod.ap.Operator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Enrique Urra C.
 */
@FindOperations
public final class TextProcessorRegistry
{
    private final List<TextVariableProcessor> processors = new ArrayList<>();

    TextProcessorRegistry()
    {
    }
    
    public TextProcessorRegistry addProcessor(TextVariableProcessor processor)
    {
        processors.add(Objects.requireNonNull(processor));
        return this;
    }
    
    @Operator
    public TextProcessorRegistry addProcessors(TextVariableProcessor... processors)
    {
        for(TextVariableProcessor processor : processors)
            addProcessor(processor);
        
        return this;
    }
    
    String parseText(String inputToCheck)
    {
        String input = (String)inputToCheck;
        Pattern regex = Pattern.compile("\\{(.*?)\\}");
        Matcher regexMatcher = regex.matcher(input);
        ArrayList<String> findings = new ArrayList<>();

        while(regexMatcher.find())
        {
            String find = regexMatcher.group();

            if (!findings.contains(find))
                findings.add(find);
        }

        String newInput = input;

        if(!findings.isEmpty())
        {
            for(String find : findings)
            {
                String withoutBraces = find.substring(1, find.length() - 1);
                String replacement = null;
                int count = processors.size();

                for(int i = 0; i < count && replacement == null; i++)
                    replacement = processors.get(i).process(withoutBraces);

                if(replacement != null)
                    newInput = newInput.replace(find, replacement);
            }
        }

        return newInput;
    }
}

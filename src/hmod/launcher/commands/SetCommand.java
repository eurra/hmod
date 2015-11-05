
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.Command;
import hmod.launcher.CommandInfo;
import hmod.launcher.Launcher;
import hmod.launcher.components.VariableData;
import optefx.util.output.OutputManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="set",
    usage="set <var_name> '<var_value>'",
    description="Sets the value for a new variable, or replaces the current one.\n"
    + "<var_name>: The variable name.\n"
    + "<var_value>: the value to set."
)
public class SetCommand extends Command
{
    private VariableData variableHandler;

    public void setVariableData(VariableData handler)
    {
        this.variableHandler = handler;
    }
    
    public void executeCommand(String[] args) throws LauncherException
    {
        if(args.length < 2)
            throw new LauncherException("Usage: " + getUsage());
        
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(args[0]);
        
        if(matcher.find())
            throw new LauncherException("The variable name cannot contain spaces");
        
        variableHandler.getVariableManager().setVariable(args[0], args[1]);
        OutputManager.println(Launcher.OUT_COMMON, "Variable set (" + args[0] + "=" + args[1] + ")");
    }
}
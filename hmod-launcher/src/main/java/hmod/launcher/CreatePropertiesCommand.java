
package hmod.launcher;

import java.util.Properties;
import optefx.util.output.OutputManager;

/**
 * Implements the 'add_file_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="createProps",
    usage="createProps <var_name>",
    description="Creates a new properties object and associate it to a variable.\n"
    + "<id>: the variable name to be associated with the properties object."
)
public class CreatePropertiesCommand extends Command
{
    private final VariableRegistry variableHandler;

    CreatePropertiesCommand(VariableRegistry variableHandler)
    {
        this.variableHandler = variableHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
            throw new CommandUsageException(this);
        
        String varName = args.getString(0);
        Properties newProp = new Properties();
        variableHandler.setVariable(varName, newProp);
        
        OutputManager.println(Launcher.OUT_COMMON, "Properties added with variable name '" + varName + "'.");
    }    
}
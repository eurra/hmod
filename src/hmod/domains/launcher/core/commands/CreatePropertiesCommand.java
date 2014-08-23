
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.VariableHandler;
import java.util.Properties;
import optefx.util.output.OutputManager;

/**
 * Implements the 'add_file_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="create_props",
    usage="create_props <var_name>",
    description="Creates a new properties object and associate it to a variable.\n"
    + "<id>: the variable name to be associated with the properties object."
)
public class CreatePropertiesCommand extends Command
{
    private VariableHandler variableHandler;
    
    public void setVariableHandler(VariableHandler variableHandler)
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
        variableHandler.getActiveVariables().setVariable(varName, newProp);
        
        OutputManager.println(Launcher.OUT_COMMON, "Properties added with variable name '" + varName + "'.");
    }    
}

package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.VariableCollection;
import hmod.domains.launcher.core.ac.VariableHandler;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="list_vars",
    usage="list_vars",
    description="Lists the variables currently set."
)
public class ListVarsCommand extends Command
{
    private VariableHandler variableHandler;

    public void setVariableHandler(VariableHandler variableHandler)
    {
        this.variableHandler = variableHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        VariableCollection activeCollection = variableHandler.getActiveVariables();
        String[] vars = activeCollection.getAllVariables();
        
        if(vars.length == 0)
        {
            OutputManager.println(Launcher.OUT_COMMON, "No variables has been set.");
        }
        else
        {
            for(int i = 0; i < vars.length; i++)
                OutputManager.println(Launcher.OUT_COMMON, vars[i] + " = '" + activeCollection.getValue(vars[i]) + "'");
        }
    }
}
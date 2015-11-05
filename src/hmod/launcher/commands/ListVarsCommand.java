
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.Command;
import hmod.launcher.CommandInfo;
import hmod.launcher.Launcher;
import hmod.launcher.VariableManager;
import hmod.launcher.components.VariableData;
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
    private VariableData variableHandler;

    public void setVariableData(VariableData handler)
    {
        this.variableHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        VariableManager manager = variableHandler.getVariableManager();
        String[] vars = manager.getVariables();
        
        if(vars.length == 0)
        {
            OutputManager.println(Launcher.OUT_COMMON, "No variables has been set.");
        }
        else
        {
            for(int i = 0; i < vars.length; i++)
                OutputManager.println(Launcher.OUT_COMMON, vars[i] + " = '" + manager.getVariableValue(vars[i]) + "'");
        }
    }
}
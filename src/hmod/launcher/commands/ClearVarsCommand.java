
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.Command;
import hmod.launcher.CommandInfo;
import hmod.launcher.Launcher;
import hmod.launcher.components.VariableData;
import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="clear_vars",
    usage="clear_vars",
    description="Clears the variables currently set."
)
public class ClearVarsCommand extends Command
{
    private VariableData variableHandler;

    public void setVariableData(VariableData handler)
    {
        this.variableHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        variableHandler.getVariableManager().clearVariables();
        OutputManager.println(Launcher.OUT_COMMON, "Variables cleared.");
    }
}
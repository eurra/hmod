
package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.VariableHandler;
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
    private VariableHandler variableHandler;

    public void setVariableHandler(VariableHandler variableHandler)
    {
        this.variableHandler = variableHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        variableHandler.getActiveVariables().clearAll();
        OutputManager.println(Launcher.OUT_COMMON, "Variables cleared.");
    }
}
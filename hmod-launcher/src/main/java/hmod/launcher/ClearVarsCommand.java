
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="clearVars",
    usage="clearVars",
    description="Clears the variables currently set."
)
public class ClearVarsCommand extends Command
{
    private final VariableRegistry variableHandler;

    ClearVarsCommand(VariableRegistry variableHandler)
    {
        this.variableHandler = variableHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        variableHandler.clearAll();
        OutputManager.println(Launcher.OUT_COMMON, "Variables cleared.");
    }
}
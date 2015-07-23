
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 *
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="listVars",
    usage="listVars",
    description="Lists the variables currently set."
)
class ListVarsCommand extends Command
{
    private final VariableHandler variableHandler;

    public ListVarsCommand(VariableHandler variableHandler)
    {
        this.variableHandler = variableHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        String[] vars = variableHandler.getAllVariables();
        
        if(vars.length == 0)
        {
            OutputManager.println(Launcher.OUT_COMMON, "No variables has been set.");
        }
        else
        {
            for(int i = 0; i < vars.length; i++)
                OutputManager.println(Launcher.OUT_COMMON, vars[i] + " = '" + variableHandler.getValue(vars[i]) + "'");
        }
    }
}
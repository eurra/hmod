
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
    word="import_vars",
    usage="import_vars <new_var0> <new_var1> ... <new_varN>",
    description="Reads N batch-input variables ($0,$1,...,$N) and assigns them to specific local variables.\n"
    + " <new_var0> <new_var1> ... <new_varN>: local variables in which input variables will be assigned. The order in"
    + "     which local variables are specified is related to the order of the batch-input variables."
)
public class ImportVarsCommand extends Command
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
        int varsToAssign = args.getCount();
        
        for(int i = 0; i < varsToAssign; i++)
            activeCollection.setVariable(args.getString(i), activeCollection.getValue("$" + i));
        
        OutputManager.println(Launcher.OUT_COMMON, varsToAssign + " variable(s) imported.");
    }
}

package hmod.domains.launcher.graph.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.VariableHandler;
import hmod.domains.launcher.graph.AlgorithmScriptLoaderCommand;
import hmod.domains.launcher.graph.RunSession;
import optefx.util.output.OutputManager;

/**
 * Implements the 'add_file_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="create_run_session",
    usage="create_run_session <var_name>",
    description="Creates a new run session and associates it to a variable.\n"
    + "<var_name>: the variable name to be associated with the session."
)
public class CreateRunSession extends AlgorithmScriptLoaderCommand
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
        variableHandler.getActiveVariables().setVariable(varName, new RunSession());
        
        OutputManager.println(Launcher.OUT_COMMON, "Run session set with variable name '" + varName + "'.");
    }    
}
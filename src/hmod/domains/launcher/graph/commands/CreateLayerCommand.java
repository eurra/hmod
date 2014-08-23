
package hmod.domains.launcher.graph.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.VariableHandler;
import hmod.domains.launcher.graph.AlgorithmLayer;
import hmod.domains.launcher.graph.AlgorithmScriptLoaderCommand;
import hmod.domains.launcher.graph.AlgorithmScriptLoader;
import optefx.util.output.OutputManager;

/**
 * Implements the 'add_file_output' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="create_layer",
    usage="create_layer <var_name>",
    description="Creates a new layer and associate it to a variable.\n"
    + "<id>: the variable name to be associated with the layer."
)
public class CreateLayerCommand extends AlgorithmScriptLoaderCommand
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
        AlgorithmLayer layer = ((AlgorithmScriptLoader)getAlgorithmLoaderHandler().getCurrentLoader()).createLayer();
        variableHandler.getActiveVariables().setVariable(varName, layer);
        
        OutputManager.println(Launcher.OUT_COMMON, "Layer set with variable name '" + varName + "'.");
    }    
}
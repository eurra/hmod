
package hmod.domains.launcher.graph.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.graph.AlgorithmLayer;
import hmod.domains.launcher.graph.AlgorithmScriptLoaderCommand;
import hmod.domains.launcher.graph.RunSession;
import optefx.util.output.OutputManager;

/**
 * Implements the 'run' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="add_to_run",
    usage="add_to_run <session> <layer> <node_id>",
    description="adds a starting step to a run session.\n"
        + "<session>: the session in which the step will be added.\n"
        + "<layer>: the layer instance from which the step will be obtained.\n"
        + "<node_id>: the node that points to the step."
)
public class AddToRunCommand extends AlgorithmScriptLoaderCommand
{    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {      
        if(args.getCount() != 3)
            throw new CommandUsageException(this);
        
        RunSession runSession = args.getArgAs(0, RunSession.class);
        AlgorithmLayer layer = args.getArgAs(1, AlgorithmLayer.class);
        String nodeStr = args.getString(2);
        
        if(!layer.hasNode(nodeStr))
            throw new LauncherException("The node id '" + nodeStr + "' has not been configured. Check if all required scripts are loaded.");

        runSession.addStep(layer.node(nodeStr));
        OutputManager.println(Launcher.OUT_COMMON, "Step in node '" + nodeStr + "' added to run session.");
    }
}
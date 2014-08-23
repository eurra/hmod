
package hmod.domains.launcher.graph.commands;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.builderFor;
import flexbuilders.core.NestedBuilder;
import flexbuilders.graph.NodeBuilder;
import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.graph.AlgorithmLayer;
import hmod.domains.launcher.graph.AlgorithmScriptLoaderCommand;
import optefx.util.output.OutputManager;

/**
 * 
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="add_array_node",
    usage="add_array_node <layer> <node_id> = [<value> | <layer_ref> <node_id_ref>]",
    description="Adds an elemento to an array builder node.\n"
    + "<layer>: the variable that stores the layer.\n"
    + "<node_id>: the node id, commonly as an enum string representation.\n"
    + "[<value> | <layer_ref> <node_id_ref>]: a value, which can be a string or a "
    + "node reference."
)
public class AddArrayNodeCommand extends AlgorithmScriptLoaderCommand
{
    private NestedBuilder retrieveNode(Object layer, Object nodeId, boolean asRef)
    {
        if(!(layer instanceof AlgorithmLayer) || !(nodeId instanceof String))
            throw new CommandUsageException(this);
        
        AlgorithmLayer handler = (AlgorithmLayer)layer;
        String nodeIdStr = (String)nodeId;
        
        if(asRef)
            return handler.ref(nodeIdStr);
        else
            return handler.node(nodeIdStr);
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 4 || args.getCount() > 5 || !(args.getString(2) instanceof String) || !args.getString(2).equals("="))
            throw new CommandUsageException(this);
        
        NodeBuilder node = (NodeBuilder)retrieveNode(args.getObject(0), args.getObject(1), false);
        ArrayBuilder arrayBuilder = node.getValueAs(ArrayBuilder.class);
        
        if(args.getCount() == 4)
        {
            Object value = args.getObject(3);
            
            if(value instanceof NestedBuilder)
                arrayBuilder.elem((NestedBuilder)value);
            else
                arrayBuilder.elem(builderFor(value));
        }
        else
        {
            arrayBuilder.elem(retrieveNode(args.getObject(3), args.getObject(4), true));
        }
        
        OutputManager.println(Launcher.OUT_COMMON, "Input set to array node (" + args.getString(1) + ")");
    }    
}
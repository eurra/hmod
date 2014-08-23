
package hmod.domains.launcher.graph.ac;

import hmod.domains.launcher.core.ac.*;
import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.*;
import static flexbuilders.basic.MetadataBuilders.metadata;
import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;
import hmod.domains.launcher.graph.SingleLayerScript;
import hmod.core.HModConfig;
import hmod.domains.launcher.core.AlgorithmLoaderInfo;
import hmod.domains.launcher.graph.AlgorithmLayer;
import hmod.domains.launcher.graph.AlgorithmScriptLoader;
import static hmod.domains.launcher.graph.AlgorithmScriptLoader.ENTRY_SCRIPTS_PATH;
import hmod.domains.launcher.graph.commands.AddArrayNodeCommand;
import hmod.domains.launcher.graph.commands.AddToRunCommand;
import hmod.domains.launcher.graph.commands.CreateLayerCommand;
import hmod.domains.launcher.graph.commands.CreateRunSession;
import hmod.domains.launcher.graph.commands.RunCommand;
import hmod.domains.launcher.graph.commands.SetNodeCommand;

/**
 *
 * @author Enrique Urra C.
 */
public class GraphExtensionScript extends SingleLayerScript
{
    public GraphExtensionScript(AlgorithmLayer input)
    {
        super(input);
    }
    
    @Override
    public void process() throws BuildException
    {
        NestedBuilder algorithmLoaderData = ref(LauncherIds.ALGORITHM_LOADER_DATA);
        NestedBuilder outputConfigData = ref(LauncherIds.OUTPUT_CONFIG_DATA);
        NestedBuilder interfaceData = ref(LauncherIds.INTERFACES_DATA);
        NestedBuilder runnerData = ref(LauncherIds.RUNNERS_DATA);
        NestedBuilder randomData = ref(LauncherIds.RANDOM_DATA);
        NestedBuilder variableData = ref(LauncherIds.VARIABLE_DATA);
        
        HModConfig config = HModConfig.getInstance();
        String scriptsPaths = config.getEntry(ENTRY_SCRIPTS_PATH, "scripts");
        String[] paths = scriptsPaths.split(";");
        
        node(LauncherIds.LOADERS_ARRAY_CONFIG).getValueAs(ArrayBuilder.class).
            elem(
                metadata().
                attachData(
                    AlgorithmLoaderInfo.builder().
                    setId("graph").
                    setName("GraphHandler-based loader").
                    setDescription("A loader based on the GraphHandler component, which uses 'graph scripts' for algorithm loading")
                ).
                setTarget(
                   object(AlgorithmScriptLoader.class).nextArgument(builderFor(paths))
                )
            );
        
        node(LauncherIds.COMMANDS_ARRAY_CONFIG).getValueAs(ArrayBuilder.class).
            elem(setFor(object(AddToRunCommand.class)).values(algorithmLoaderData)).
            elem(setFor(object(CreateLayerCommand.class)).values(algorithmLoaderData, variableData)).
            elem(setFor(object(CreateRunSession.class)).values(algorithmLoaderData, variableData)).
            elem(setFor(object(RunCommand.class)).values(outputConfigData, interfaceData, runnerData, randomData)).
            elem(setFor(object(SetNodeCommand.class)).values(algorithmLoaderData)).
            elem(setFor(object(AddArrayNodeCommand.class)).values(algorithmLoaderData));
    }
}

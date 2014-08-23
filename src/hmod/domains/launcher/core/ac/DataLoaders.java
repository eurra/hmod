
package hmod.domains.launcher.core.ac;

import static flexbuilders.basic.BasicBuilders.object;
import static flexbuilders.basic.BasicBuilders.setFor;
import flexbuilders.core.NestedBuilder;
import flexbuilders.graph.BuilderGraph;
import hmod.domains.launcher.core.AlgorithmLoader;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.TextVariableProcessor;
import hmod.domains.launcher.core.running.AlgorithmInterfaceFactory;
import hmod.domains.launcher.core.running.AlgorithmRunnerFactory;

/**
 *
 * @author Enrique Urra C.
 */
class DataLoaders
{    
    public static NestedBuilder<AlgorithmLoaderHandler> algorithmLoaderHandlerLoader(BuilderGraph graph)
    {
        return object(
            DefaultAlgorithmLoaderHandler.class, 
            graph.getAllFromNode(LauncherIds.LOADERS_ARRAY_CONFIG, AlgorithmLoader.class)
        );
    }
    
    public static NestedBuilder<LauncherHandler> launcherHandlerLoader(BuilderGraph graph)
    {
        return object(DefaultLauncherHandler.class,
            graph.getNode(LauncherIds.DEBUG_CONFIG),
            graph.getNode(LauncherIds.THREADING_CONFIG),
            graph.getNode(LauncherIds.BATCH_PATH_CONFIG),
            graph.getNode(LauncherIds.OUTPUT_PATH_CONFIG)
        );
    }
    
    public static NestedBuilder<VariableHandler> variablerHandlerLoader(BuilderGraph graph)
    {
        NestedBuilder commandBufferData = graph.loadNode(LauncherIds.COMMANDS_BUFFER_DATA);
        NestedBuilder<VariableHandler> builder = object(DefaultVariableHandler.class);
        
        return setFor(builder).values(commandBufferData);
    }
    
    public static NestedBuilder<WindowsHandler> windowsHandlerLoader(BuilderGraph graph)
    {
        return object(
            DefaultWindowsHandler.class,
            graph.getNode(LauncherIds.WINDOWS_AUTOCLOSE_CONFIG)
        );
    }
    
    public static NestedBuilder<RandomHandler> randomHandlerLoader(BuilderGraph graph)
    {
        return object(DefaultRandomHandler.class);
    }
    
    public static NestedBuilder<RunnerHandler> runnerHandlerLoader(BuilderGraph graph)
    {
        return object(
            DefaultRunnerHandler.class,
            graph.getAllFromNode(LauncherIds.RUNNERS_ARRAY_CONFIG, AlgorithmRunnerFactory.class),
            graph.getNode(LauncherIds.RUNNER_ID_CONFIG)
        );
    }
    
    public static NestedBuilder<RunInterfaceHandler> interfaceHandlerLoader(BuilderGraph graph)
    {
        return object(DefaultInterfaceHandler.class,
            graph.getAllFromNode(LauncherIds.INTERFACES_ARRAY_CONFIG, AlgorithmInterfaceFactory.class),
            graph.getNode(LauncherIds.INTERFACE_ID_CONFIG)
        );
    }
    
    public static NestedBuilder<OutputConfigHandler> outputConfigHandlerLoader(BuilderGraph graph)
    {
        return object(DefaultOutputConfigHandler.class);
    }
    
    public static NestedBuilder<CommandHandler> commandHandlerLoader(BuilderGraph graph)
    {
        NestedBuilder<CommandHandler> builder = object(DefaultCommandHandler.class);
        
        return setFor(builder).
            values(graph.getAllFromNode(LauncherIds.COMMANDS_ARRAY_CONFIG, Command.class));
    }
    
    public static NestedBuilder<CommandInfoHandler> commandInfoHandlerLoader(BuilderGraph graph)
    {
        NestedBuilder commandData = graph.loadNode(LauncherIds.COMMANDS_DATA);
        NestedBuilder<CommandInfoHandler> builder = object(DefaultCommandInfoHandler.class);
        
        return setFor(builder).values(commandData);
    }
    
    public static NestedBuilder<CommandBufferHandler> commandBufferHandlerLoader(BuilderGraph graph)
    {
        NestedBuilder variableData = graph.loadNode(LauncherIds.VARIABLE_DATA);
        NestedBuilder<CommandBufferHandler> builder = object(DefaultCommandBufferHandler.class);
        
        return setFor(builder).values(variableData);
    }
    
    public static NestedBuilder<CommandParseHandler> commandParseHandlerLoader(BuilderGraph graph)
    {
        NestedBuilder commandData = graph.loadNode(LauncherIds.COMMANDS_DATA);
        NestedBuilder variableData = graph.loadNode(LauncherIds.VARIABLE_DATA);
        NestedBuilder builder = object(DefaultCommandParseHandler.class).
            nextArgument(graph.getAllFromNode(LauncherIds.VARIABLE_PROCESSORS_ARRAY_CONFIG, TextVariableProcessor.class));
        
        return setFor(builder).values(commandData, variableData);
    }
}
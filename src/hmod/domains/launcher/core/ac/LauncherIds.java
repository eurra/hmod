
package hmod.domains.launcher.core.ac;

import static flexbuilders.graph.GraphFactory.nodeId;
import flexbuilders.graph.NodeLoader;
import flexbuilders.graph.NodeId;
import hmod.core.Step;
import hmod.domains.launcher.core.AlgorithmLoader;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.TextVariableProcessor;
import hmod.domains.launcher.core.running.AlgorithmInterfaceFactory;
import hmod.domains.launcher.core.running.AlgorithmRunnerFactory;

/**
 *
 * @author Enrique Urra C.
 */
public final class LauncherIds
{
    public static final NodeLoader<LauncherHandler> LAUNCHER_DATA = DataLoaders::launcherHandlerLoader;
    public static final NodeLoader<VariableHandler> VARIABLE_DATA = DataLoaders::variablerHandlerLoader;
    public static final NodeLoader<WindowsHandler> WINDOWS_DATA = DataLoaders::windowsHandlerLoader;
    public static final NodeLoader<RandomHandler> RANDOM_DATA = DataLoaders::randomHandlerLoader;
    public static final NodeLoader<RunnerHandler> RUNNERS_DATA = DataLoaders::runnerHandlerLoader;
    public static final NodeLoader<RunInterfaceHandler> INTERFACES_DATA = DataLoaders::interfaceHandlerLoader;
    public static final NodeLoader<OutputConfigHandler> OUTPUT_CONFIG_DATA = DataLoaders::outputConfigHandlerLoader;
    public static final NodeLoader<CommandHandler> COMMANDS_DATA = DataLoaders::commandHandlerLoader;
    public static final NodeLoader<CommandInfoHandler> COMMANDS_INFO_DATA = DataLoaders::commandInfoHandlerLoader;
    public static final NodeLoader<CommandBufferHandler> COMMANDS_BUFFER_DATA = DataLoaders::commandBufferHandlerLoader;
    public static final NodeLoader<CommandParseHandler> COMMANDS_PARSER_DATA = DataLoaders::commandParseHandlerLoader;
    public static final NodeLoader<AlgorithmLoaderHandler> ALGORITHM_LOADER_DATA = DataLoaders::algorithmLoaderHandlerLoader;
    
    public static final NodeId<Command> COMMANDS_ARRAY_CONFIG = nodeId();
    public static final NodeId<TextVariableProcessor> VARIABLE_PROCESSORS_ARRAY_CONFIG = nodeId();
    public static final NodeId<AlgorithmRunnerFactory> RUNNERS_ARRAY_CONFIG = nodeId();
    public static final NodeId<AlgorithmInterfaceFactory> INTERFACES_ARRAY_CONFIG = nodeId();
    public static final NodeId<AlgorithmLoader> LOADERS_ARRAY_CONFIG = nodeId();
    
    public static final NodeId<String> BATCH_PATH_CONFIG = nodeId();
    public static final NodeId<String> OUTPUT_PATH_CONFIG = nodeId();
    public static final NodeId<Boolean> DEBUG_CONFIG = nodeId();
    public static final NodeId<Boolean> THREADING_CONFIG = nodeId();
    public static final NodeId<String> RUNNER_ID_CONFIG = nodeId();
    public static final NodeId<String> INTERFACE_ID_CONFIG = nodeId();
    public static final NodeId<Boolean> WINDOWS_AUTOCLOSE_CONFIG = nodeId();
    
    public static final NodeLoader<Step> MAIN_START = MainLoader::load;
}

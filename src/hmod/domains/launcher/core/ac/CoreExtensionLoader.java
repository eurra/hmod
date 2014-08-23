
package hmod.domains.launcher.core.ac;

import static flexbuilders.basic.BasicBuilders.*;
import flexbuilders.core.BuildException;
import flexbuilders.core.NestedBuilder;
import flexbuilders.graph.BuilderGraph;
import hmod.domains.launcher.core.LauncherExtensionLoader;
import hmod.domains.launcher.core.commands.AddFileOutputCommand;
import hmod.domains.launcher.core.commands.AddOutputCommand;
import hmod.domains.launcher.core.commands.BatchCommand;
import hmod.domains.launcher.core.commands.ClearOutputsCommand;
import hmod.domains.launcher.core.commands.ClearVarsCommand;
import hmod.domains.launcher.core.commands.CreatePropertiesCommand;
import hmod.domains.launcher.core.commands.DebugCommand;
import hmod.domains.launcher.core.commands.EchoCommand;
import hmod.domains.launcher.core.commands.EndRepeatCommand;
import hmod.domains.launcher.core.commands.ExitCommand;
import hmod.domains.launcher.core.commands.HelpCommand;
import hmod.domains.launcher.core.commands.ImportVarsCommand;
import hmod.domains.launcher.core.commands.ListVarsCommand;
import hmod.domains.launcher.core.commands.LoadCommand;
import hmod.domains.launcher.core.commands.RandomSeedCommand;
import hmod.domains.launcher.core.commands.RepeatCommand;
import hmod.domains.launcher.core.commands.RequireSetCommand;
import hmod.domains.launcher.core.commands.ResetCommand;
import hmod.domains.launcher.core.commands.SetCommand;
import hmod.domains.launcher.core.commands.SetInterfaceCommand;
import hmod.domains.launcher.core.commands.SetPropertiesCommand;
import hmod.domains.launcher.core.commands.SetRunnerCommand;
import hmod.domains.launcher.core.commands.ThreadingCommand;
import hmod.domains.launcher.core.commands.WindowsConfigCommand;
import hmod.domains.launcher.core.running.DefaultAlgorithmInterfaceFactory;
import hmod.domains.launcher.core.running.DefaultAlgorithmRunnerFactory;
import hmod.domains.launcher.core.running.WindowedAlgorithmInterfaceFactory;
import hmod.domains.launcher.core.textproc.BatchEnvironmentProcessor;
import hmod.domains.launcher.core.textproc.DateComponentProcessor;
import hmod.domains.launcher.core.textproc.DefaultFoldersProcessor;
import hmod.domains.launcher.core.textproc.RandomProcessor;
import static hmod.loader.graph.AlgorithmBuilders.dataHandlerSetter;

/**
 *
 * @author Enrique Urra C.
 */
public class CoreExtensionLoader
{    
    @LauncherExtensionLoader
    public static void load(BuilderGraph graph) throws BuildException
    {          
        NestedBuilder launcherData = graph.loadNode(LauncherIds.LAUNCHER_DATA);
        NestedBuilder algorithmLoaderData = graph.loadNode(LauncherIds.ALGORITHM_LOADER_DATA);
        NestedBuilder outputConfigData = graph.loadNode(LauncherIds.OUTPUT_CONFIG_DATA);
        NestedBuilder commandInfoData = graph.loadNode(LauncherIds.COMMANDS_INFO_DATA);
        NestedBuilder commandParserData = graph.loadNode(LauncherIds.COMMANDS_PARSER_DATA);
        NestedBuilder commandBufferData = graph.loadNode(LauncherIds.COMMANDS_BUFFER_DATA);
        NestedBuilder interfaceData = graph.loadNode(LauncherIds.INTERFACES_DATA);
        NestedBuilder runnerData = graph.loadNode(LauncherIds.RUNNERS_DATA);
        NestedBuilder randomData = graph.loadNode(LauncherIds.RANDOM_DATA);
        NestedBuilder variableData = graph.loadNode(LauncherIds.VARIABLE_DATA);
        NestedBuilder windowsData = graph.loadNode(LauncherIds.WINDOWS_DATA);
        
        /**********************************************************************
         * SECTION I. Commands' definitions
         **********************************************************************/
        
        graph.forNode(LauncherIds.COMMANDS_ARRAY_CONFIG).
            setValue(dataHandlerSetter(object(AddOutputCommand.class)).setData(outputConfigData)).
            setValue(dataHandlerSetter(object(AddFileOutputCommand.class)).setData(outputConfigData)).
            setValue(dataHandlerSetter(object(ClearOutputsCommand.class)).setData(outputConfigData)).
            setValue(dataHandlerSetter(object(BatchCommand.class)).setData(launcherData, commandBufferData, commandParserData)).
            setValue(dataHandlerSetter(object(ExitCommand.class)).setData(launcherData)).
            setValue(dataHandlerSetter(object(HelpCommand.class)).setData(commandInfoData)).
            setValue(dataHandlerSetter(object(LoadCommand.class)).setData(algorithmLoaderData)).
            setValue(dataHandlerSetter(object(ResetCommand.class)).setData(algorithmLoaderData)).
            setValue(dataHandlerSetter(object(DebugCommand.class)).setData(launcherData)).
            setValue(dataHandlerSetter(object(RepeatCommand.class)).setData(commandBufferData)).
            setValue(dataHandlerSetter(object(EndRepeatCommand.class)).setData(commandBufferData)).
            setValue(object(EchoCommand.class)).
            setValue(dataHandlerSetter(object(SetCommand.class)).setData(variableData)).
            setValue(dataHandlerSetter(object(ListVarsCommand.class)).setData(variableData)).
            setValue(dataHandlerSetter(object(ClearVarsCommand.class)).setData(variableData)).
            setValue(dataHandlerSetter(object(WindowsConfigCommand.class)).setData(windowsData)).
            setValue(dataHandlerSetter(object(ThreadingCommand.class)).setData(launcherData)).
            setValue(dataHandlerSetter(object(SetRunnerCommand.class)).setData(runnerData)).
            setValue(dataHandlerSetter(object(SetInterfaceCommand.class)).setData(interfaceData)).
            setValue(dataHandlerSetter(object(RandomSeedCommand.class)).setData(randomData)).
            setValue(dataHandlerSetter(object(RequireSetCommand.class)).setData(variableData)).
            setValue(dataHandlerSetter(object(CreatePropertiesCommand.class)).setData(variableData)).
            setValue(dataHandlerSetter(object(ImportVarsCommand.class)).setData(variableData)).
            setValue(object(SetPropertiesCommand.class));
        
        /**********************************************************************
         * SECTION II. Variable processors' definitions
         **********************************************************************/
        
        graph.forNode(LauncherIds.VARIABLE_PROCESSORS_ARRAY_CONFIG).
            setValue(builderFor(new DateComponentProcessor())).
            setValue(dataHandlerSetter(object(DefaultFoldersProcessor.class)).setData(launcherData)).
            setValue(dataHandlerSetter(object(BatchEnvironmentProcessor.class)).setData(commandBufferData)).
            setValue(dataHandlerSetter(object(RandomProcessor.class)).setData(randomData));
        
        /**********************************************************************
         * SECTION III. Runner factories' definitions
         **********************************************************************/
        
        graph.forNode(LauncherIds.RUNNERS_ARRAY_CONFIG).
            setValue(builderFor(new DefaultAlgorithmRunnerFactory()));
        
        /**********************************************************************
         * SECTION IV. Interface factories' definitions
         **********************************************************************/
        
        graph.forNode(LauncherIds.INTERFACES_ARRAY_CONFIG).
            setValue(builderFor(new DefaultAlgorithmInterfaceFactory())).
            setValue(dataHandlerSetter(object(WindowedAlgorithmInterfaceFactory.class)).setData(launcherData, windowsData));
    }
}

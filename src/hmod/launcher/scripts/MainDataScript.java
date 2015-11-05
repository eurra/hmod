
package hmod.launcher.scripts;

import static flexbuilders.basic.BasicBuilders.*;
import static flexbuilders.basic.SetterBuilders.*;
import flexbuilders.tree.BranchBuilderSet;
import flexbuilders.core.BuildException;
import flexbuilders.scripting.BuildScript;
import flexbuilders.tree.TreeHandler;
import hmod.launcher.Command;
import hmod.launcher.Plugin;
import hmod.launcher.VariableProcessor;
import hmod.launcher.components.CommandData;
import hmod.launcher.components.LauncherData;
import hmod.launcher.components.PluginData;
import hmod.launcher.components.RandomData;
import hmod.launcher.components.RunData;
import hmod.launcher.components.VariableData;
import hmod.launcher.components.WindowsData;
import hmod.launcher.running.AlgorithmInterfaceFactory;
import hmod.launcher.running.AlgorithmRunnerFactory;
import hmod.core.DataObjectProxy;

/**
 *
 * @author Enrique Urra C.
 */
public class MainDataScript extends BuildScript
{
    public MainDataScript(TreeHandler input)
    {
        super(input);
    }
    
    @Override
    public void process() throws BuildException
    {
        /**********************************************************************
         * SECTION I. Data objects
         **********************************************************************/
        
        BranchBuilderSet sealedSet = enableNewSet();
        
        // Main data object definition
        Object mainDataObj = DataObjectProxy.createFor(
            LauncherData.class,
            CommandData.class,
            VariableData.class,
            WindowsData.class,
            RunData.class,
            RandomData.class
        );
        
        branch("consoleLauncher_mainData").setBuildable(
            setterInvoker(value(mainDataObj)).
            set(beanSetter().setMethodName("setCommands"), ref("consoleLauncher_commands")).
            set(beanSetter().setMethodName("setVariableProcessors"), ref("consoleLauncher_variableProcessors")).
            set(beanSetter().setMethodName("setRunnerFactories"), ref("consoleLauncher_runnerFactories")).
            set(beanSetter().setMethodName("setInterfaceFactories"), ref("consoleLauncher_interfaceFactories")).
            set(beanSetter().setMethodName("setVariableDelimiter"), ref("consoleLauncher_varDelimiter")).
            set(beanSetter().setMethodName("setBatchBaseFolder"), ref("consoleLauncher_batchPath")).
            set(beanSetter().setMethodName("setOutputBaseFolder"), ref("consoleLauncher_outputPath")).
            set(beanSetter().setMethodName("setDebug"), ref("consoleLauncher_debug")).
            set(beanSetter().setMethodName("setThreading"), ref("consoleLauncher_threading")).
            set(beanSetter().setMethodName("setDefaultRunner"), ref("consoleLauncher_defaultRunner")).
            set(beanSetter().setMethodName("setDefaultInterface"), ref("consoleLauncher_defaultInterface"))
        );
        
        // Plugins' data objects
        branch("consoleLauncher_startPluginData").setBuildable(
            setterInvoker(value(DataObjectProxy.createFor(PluginData.class))).
            set(beanSetter().setMethodName("setPlugins"), ref("consoleLauncher_startPlugins"))
        );
        
        branch("consoleLauncher_onCmdLoadPluginData").setBuildable(
            setterInvoker(value(DataObjectProxy.createFor(PluginData.class))).
            set(beanSetter().setMethodName("setPlugins"), ref("consoleLauncher_onCmdLoadPlugins"))
        );
        
        branch("consoleLauncher_onCmdExecPluginData").setBuildable(
            setterInvoker(value(DataObjectProxy.createFor(PluginData.class))).
            set(beanSetter().setMethodName("setPlugins"), ref("consoleLauncher_onCmdExecPlugins"))
        );
        
        branch("consoleLauncher_endPluginData").setBuildable(
            setterInvoker(value(DataObjectProxy.createFor(PluginData.class))).
            set(beanSetter().setMethodName("setPlugins"), ref("consoleLauncher_endPlugins"))
        );
        
        sealedSet.seal();
        
        /**********************************************************************
         * SECTION II. Array containers
         **********************************************************************/
        
        BranchBuilderSet readOnlySet = enableNewSet();
        
        // Commands array
        branch("consoleLauncher_commands").setBuildable(array(Command.class));
        
        // Variable processors array
        branch("consoleLauncher_variableProcessors").setBuildable(array(VariableProcessor.class));
        
        // Runner factories array
        branch("consoleLauncher_runnerFactories").setBuildable(array(AlgorithmRunnerFactory.class));
        
        // Interface factories array
        branch("consoleLauncher_interfaceFactories").setBuildable(array(AlgorithmInterfaceFactory.class));
        
        // Plugins arrays
        branch("consoleLauncher_startPlugins").setBuildable(array(Plugin.class));
        branch("consoleLauncher_onCmdLoadPlugins").setBuildable(array(Plugin.class));
        branch("consoleLauncher_onCmdExecPlugins").setBuildable(array(Plugin.class));
        branch("consoleLauncher_endPlugins").setBuildable(array(Plugin.class));       

        readOnlySet.readOnly();
    }
}
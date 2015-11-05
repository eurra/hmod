
package hmod.launcher.scripts;

import flexbuilders.basic.ArrayBuilder;
import static flexbuilders.basic.BasicBuilders.*;
import static flexbuilders.basic.SetterBuilders.*;
import flexbuilders.core.BuildException;
import flexbuilders.scripting.BuildScript;
import flexbuilders.tree.TreeHandler;
import hmod.launcher.commands.AddFileOutputCommand;
import hmod.launcher.commands.AddOutputCommand;
import hmod.launcher.commands.BatchCommand;
import hmod.launcher.commands.ClearOutputsCommand;
import hmod.launcher.commands.ClearVarsCommand;
import hmod.launcher.commands.DebugCommand;
import hmod.launcher.commands.EchoCommand;
import hmod.launcher.commands.EndRepeatCommand;
import hmod.launcher.commands.ExitCommand;
import hmod.launcher.commands.HelpCommand;
import hmod.launcher.commands.ListVarsCommand;
import hmod.launcher.commands.LoadCommand;
import hmod.launcher.commands.RandomSeedCommand;
import hmod.launcher.commands.RepeatCommand;
import hmod.launcher.commands.ResetCommand;
import hmod.launcher.commands.RunCommand;
import hmod.launcher.commands.SetCommand;
import hmod.launcher.commands.SetInterfaceCommand;
import hmod.launcher.commands.SetRunnerCommand;
import hmod.launcher.commands.ThreadingCommand;
import hmod.launcher.commands.WindowsConfigCommand;
import hmod.launcher.plugins.CommandLoaderPlugin;
import hmod.launcher.plugins.RunLoaderPlugin;
import hmod.launcher.plugins.VariableProcessingPlugin;
import hmod.launcher.running.DefaultAlgorithmInterfaceFactory;
import hmod.launcher.running.DefaultAlgorithmRunnerFactory;
import hmod.launcher.running.WindowedAlgorithmInterfaceFactory;
import hmod.launcher.varproc.BatchEnvProcessor;
import hmod.launcher.varproc.DateComponentProcessor;
import hmod.launcher.varproc.DefaultFoldersProcessor;
import hmod.launcher.varproc.RandomProcessor;

/**
 *
 * @author Enrique Urra C.
 */
public class CoreExtensionScript extends BuildScript
{
    public CoreExtensionScript(TreeHandler input)
    {
        super(input);
    }
    
    @Override
    public void process() throws BuildException
    {        
        /**********************************************************************
         * SECTION I. Commands' definitions
         **********************************************************************/
        
        branch("consoleLauncher_commands").getBuildableAs(ArrayBuilder.class).
        elem(
            setterInvoker(value(new AddOutputCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new AddFileOutputCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new BatchCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new ClearOutputsCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new ExitCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new HelpCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new LoadCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new ResetCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new RunCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new DebugCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new RepeatCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new EndRepeatCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(value(new EchoCommand())).
        elem(
            setterInvoker(value(new SetCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new ListVarsCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new ClearVarsCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new WindowsConfigCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new ThreadingCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new SetRunnerCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new SetInterfaceCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new RandomSeedCommand())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        );
        
        /**********************************************************************
         * SECTION II. Variable processors' definitions
         **********************************************************************/
        
        branch("consoleLauncher_variableProcessors").getBuildableAs(ArrayBuilder.class).
        elem(value(new DateComponentProcessor())).
        elem(
            setterInvoker(value(new DefaultFoldersProcessor())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new BatchEnvProcessor())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new RandomProcessor())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        );
        
        /**********************************************************************
         * SECTION III. Runner factories' definitions
         **********************************************************************/
        
        branch("consoleLauncher_runnerFactories").getBuildableAs(ArrayBuilder.class).
        elem(value(new DefaultAlgorithmRunnerFactory()));
        
        /**********************************************************************
         * SECTION IV. Interface factories' definitions
         **********************************************************************/
        
        branch("consoleLauncher_interfaceFactories").getBuildableAs(ArrayBuilder.class).
        elem(value(new DefaultAlgorithmInterfaceFactory())).
        elem(
            setterInvoker(value(new WindowedAlgorithmInterfaceFactory())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        );
        
        /**********************************************************************
         * SECTION V. Plugins' definitions
         **********************************************************************/
        
        branch("consoleLauncher_startPlugins").getBuildableAs(ArrayBuilder.class).
        elem(
            setterInvoker(value(new VariableProcessingPlugin())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new CommandLoaderPlugin())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        ).
        elem(
            setterInvoker(value(new RunLoaderPlugin())).
            set(beanSetter(), ref("consoleLauncher_mainData"))
        );
    }
}

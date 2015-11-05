
package hmod.launcher.scripts;

import hmod.launcher.components.StartLauncher;
import hmod.launcher.components.CheckTerminated;
import hmod.launcher.components.ExecuteCommand;
import hmod.launcher.components.EndLauncher;
import hmod.launcher.components.ReadNextCommand;
import flexbuilders.core.BuildException;
import flexbuilders.core.Buildable;
import flexbuilders.scripting.BuildScript;
import flexbuilders.tree.BranchBuilder;
import flexbuilders.tree.TreeHandler;
import static hmod.parser.builders.AlgorithmBuilders.*;

/**
 *
 * @author Enrique Urra C.
 */
public class MainScript extends BuildScript
{
    private BranchBuilder start, readNext, execute, checkTerminated, end;
    private Buildable mainData, startPluginData, onLoadPluginData, onExecPluginData, endPluginData;

    public MainScript(TreeHandler input) throws BuildException
    {
        super(input);
        
        start = branch("consoleLauncher_main");
        readNext = branch();
        execute = branch();
        checkTerminated = branch();
        end = branch();
        
        mainData = ref("consoleLauncher_mainData");
        startPluginData = ref("consoleLauncher_startPluginData");
        onLoadPluginData = ref("consoleLauncher_onCmdLoadPluginData");
        onExecPluginData = ref("consoleLauncher_onCmdExecPluginData");
        endPluginData = ref("consoleLauncher_onCmdExecPluginData");    
    }
    
    @Override
    public void process() throws BuildException
    {
        start.setBuildable(
            sequentialStep().setNextStep(readNext).
            addOperator(
                operator(StartLauncher.class).
                injectData(startPluginData).
                injectData(mainData)
            )
        ).seal();
        
        readNext.setBuildable(
            booleanStep().setTrueStep(execute).setFalseStep(checkTerminated).
            setDecider(
                operator(ReadNextCommand.class).
                injectData(onLoadPluginData).
                injectData(mainData)
            )
        );
        
        execute.setBuildable(
            sequentialStep().setNextStep(checkTerminated).
            addOperator(
                operator(ExecuteCommand.class).
                injectData(onExecPluginData).
                injectData(mainData)
            )
        );
        
        checkTerminated.setBuildable(
            booleanStep().setTrueStep(end).setFalseStep(readNext).
            setDecider(
                operator(CheckTerminated.class).
                injectData(mainData)
            )
        );
        
        end.setBuildable(
            sequentialStep().
            addOperator(
                operator(EndLauncher.class).
                injectData(endPluginData)
            )
        );
    }
}

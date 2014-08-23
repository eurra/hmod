
package hmod.domains.launcher.graph.commands;

import hmod.core.Algorithm;
import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandUsageException;
import hmod.domains.launcher.core.ac.OutputConfigHandler;
import hmod.domains.launcher.core.ac.RandomHandler;
import hmod.domains.launcher.core.ac.RunInterfaceHandler;
import hmod.domains.launcher.core.ac.RunnerHandler;
import hmod.domains.launcher.core.running.AlgorithmInterface;
import hmod.domains.launcher.core.running.AlgorithmRunner;
import hmod.domains.launcher.graph.RunSession;
import optefx.util.output.OutputConfigBuilder;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;

/**
 * Implements the 'run' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="run",
    usage="run <run_session> [run name]",
    description="Runs a session and optionally uses the provided run name as description.\n"
        + "<run_session>: the run session to execute.\n"
        + "[run name]: An optional name for the executed algorithm."
)
public class RunCommand extends Command
{
    private OutputConfigHandler outputConfigHandler;
    private RunInterfaceHandler runInterfaceHandler;
    private RunnerHandler runnerHandler;
    private RandomHandler randomHandler;

    public void setOutputConfigHandler(OutputConfigHandler outputConfigHandler)
    {
        this.outputConfigHandler = outputConfigHandler;
    }

    public void setRandomHandler(RandomHandler randomHandler)
    {
        this.randomHandler = randomHandler;
    }

    public void setRunInterfaceHandler(RunInterfaceHandler runInterfaceHandler)
    {
        this.runInterfaceHandler = runInterfaceHandler;
    }

    public void setRunnerHandler(RunnerHandler runnerHandler)
    {
        this.runnerHandler = runnerHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {      
        if(args.getCount() < 1)
            throw new CommandUsageException(this);
        
        RunSession runSession = args.getArgAs(0, RunSession.class);
        String name = "(no name)";
        
        if(args.getCount() > 1)
            name = args.getString(1);
        
        Algorithm algorithm = runSession.build();
           
        AlgorithmRunner runner;
        AlgorithmInterface ui;
        
        try
        {
            runner = runnerHandler.createNewRunnerFromCurrent();
        }
        catch(DataHandlingException ex)
        {
            throw new LauncherException("Cannot initialize the algorithm runner", ex);
        }
        
        try
        {
            ui = runInterfaceHandler.createNewInterfaceFromCurrent(name);
        }
        catch (DataHandlingException ex)
        {
            throw new LauncherException("Cannot initialize the algorithm interface", ex);
        }
        
        OutputConfigBuilder outputConfigBuilder = outputConfigHandler.createConfigBuilder();
        ui.configOutputs(outputConfigBuilder);
        
        Thread algorithmThread = runner.getAlgorithmThread();
        
        OutputConfig outputConfig = outputConfigBuilder.
            addSystemOutputId(OutputManager.DEFAULT_ID).
            addSystemErrorOutputId(OutputManager.DEFAULT_ERROR_ID).
            build();
        
        long randomSeed = randomHandler.getCurrentSeed();

        runner.init(algorithm, outputConfig, randomSeed);
        ui.init(algorithm, algorithmThread);

        randomHandler.setRandomSeed();
    }
}
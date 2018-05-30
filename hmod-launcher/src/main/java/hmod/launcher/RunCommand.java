
package hmod.launcher;

import hmod.core.Procedure;
import optefx.util.output.OutputConfig;
import optefx.util.output.OutputManager;

/**
 * Implements the 'run' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="run",
    usage="run <algorithm> [run name]",
    description="Runs an algorithm and optionally uses the provided run name as description.\n"
        + "<algorithm>: the algorithm instance to execute.\n"
        + "[run name]: An optional name for the executed algorithm."
)
class RunCommand extends Command
{
    private final OutputSettings outputConfigHandler;
    private final InterfaceRegistry interfaceHandler;
    private final RunnerRegistry runnerHandler;
    private final RandomSeed randomHandler;

    public RunCommand(OutputSettings outputConfigHandler, 
                      InterfaceRegistry interfaceHandler, 
                      RunnerRegistry runnerHandler,
                      RandomSeed randomHandler)
    {
        this.outputConfigHandler = outputConfigHandler;
        this.interfaceHandler = interfaceHandler;
        this.runnerHandler = runnerHandler;
        this.randomHandler = randomHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {      
        if(args.getCount() < 1)
            throw new CommandUsageException(this);
        
        Procedure algorithm = args.getArgAs(0, Procedure.class);
        String name = "(no name)";
        
        if(args.getCount() > 1)
            name = args.getString(1);
           
        AlgorithmRunner runner;
        AlgorithmInterface ui;
        
        try
        {
            runner = runnerHandler.createNewRunnerFromCurrent();
        }
        catch(IllegalArgumentException ex)
        {
            throw new LauncherException("Cannot initialize the algorithm runner", ex);
        }
        
        try
        {
            ui = interfaceHandler.createNewInterfaceFromCurrent(name);
        }
        catch(IllegalArgumentException ex)
        {
            throw new LauncherException("Cannot initialize the algorithm interface", ex);
        }
        
        OutputConfig outputConfig = outputConfigHandler.createConfig();
        Runnable interfaceRunnable = ui.configure(algorithm, outputConfig);
        
        outputConfig.
            addSystemOutputId(OutputManager.DEFAULT_ID).
            addSystemErrorOutputId(OutputManager.DEFAULT_ERROR_ID);
        
        long randomSeed = randomHandler.getCurrent();
        Thread algorithmThread = runner.getAlgorithmThread(outputConfig, randomSeed, interfaceRunnable);
            
        algorithmThread.start();
        ui.start(algorithmThread);
        /*try {
            sleep(500);
        } catch(InterruptedException ex) {}*/
        
        randomHandler.setRandom();
    }
}
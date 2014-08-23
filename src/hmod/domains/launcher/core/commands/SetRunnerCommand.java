
package hmod.domains.launcher.core.commands;

import hmod.core.DataHandlingException;
import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.RunnerHandler;
import hmod.domains.launcher.core.running.RunnerInfo;
import optefx.util.output.OutputManager;

/**
 * Implements the 'set_runner' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="set_runner",
    usage="set_runner <id>",
    description="Configures the runner used during for an algorithm execution.\n"
        + "<id> The id of the runner to set. Run this command without this"
        + " argument to see all the available ids."
)
public class SetRunnerCommand extends Command
{
    private RunnerHandler runnerHandler;

    public void setRunnerHandler(RunnerHandler runnerHandler)
    {
        this.runnerHandler = runnerHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        String[] factoriesIds = runnerHandler.getSupportedRunnersIds();
        
        if(args.getCount() < 1)
        {
            for(int i = 0; i < factoriesIds.length; i++)
            {
                RunnerInfo factory;
                
                try
                {                
                    factory = runnerHandler.getRunnerInfoFor(factoriesIds[i]); 
                }
                catch(DataHandlingException ex)
                {
                    throw new LauncherException(ex);
                }
                
                OutputManager.println(Launcher.OUT_COMMON, "'" + factory.id()+ "': " + factory.description());
            }
        }
        else
        {
            String id = args.getString(0);
            
            try
            {
                runnerHandler.setCurrentRunner(id);
                OutputManager.println(Launcher.OUT_COMMON, "Runner '" + id + "' selected.");
            }
            catch(DataHandlingException ex)
            {
                OutputManager.println(Launcher.OUT_COMMON, ex.getLocalizedMessage());
            }
        }
    }
}
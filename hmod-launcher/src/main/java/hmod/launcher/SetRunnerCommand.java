
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 * Implements the 'set_runner' launcher command.
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="setRunner",
    usage="setRunner <id>",
    description="Configures the runner used during for an algorithm execution.\n"
        + "<id> The id of the runner to set. Run this command without this"
        + " argument to see all the available ids."
)
class SetRunnerCommand extends Command
{
    private final RunnerRegistry runnerHandler;

    public SetRunnerCommand(RunnerRegistry runnerHandler)
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
                catch(IllegalArgumentException ex)
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
            catch(IllegalArgumentException ex)
            {
                OutputManager.println(Launcher.OUT_COMMON, ex.getLocalizedMessage());
            }
        }
    }
}

package hmod.domains.launcher.core.commands;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.CommandInfo;
import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.CommandArgs;
import hmod.domains.launcher.core.Launcher;
import hmod.domains.launcher.core.ac.RandomHandler;
import optefx.util.output.OutputManager;

/**
 * Implements the 'random_seed' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="random_seed",
    usage="random_seed [seed]",
    description="Sets or gets the random seed to be used in the next algorithm "
        + "run.\n"
        + "[seed] The seed to set. Fi this argument is not provided, the next "
        + "seed to be used will be reported."
)
public class RandomSeedCommand extends Command
{    
    private RandomHandler randomHandler;

    public void setRandomHandler(RandomHandler randomHandler)
    {
        this.randomHandler = randomHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
        {
            OutputManager.println(Launcher.OUT_COMMON, "Next seed: " + randomHandler.getCurrentSeed()+ ".");
        }
        else
        {
            try
            {
                long seed = args.getArgAs(0, Long.class);
                randomHandler.setCurrentSeed(seed);
                OutputManager.println(Launcher.OUT_COMMON, "New seed configurated: " + seed + ".");
            }
            catch(NumberFormatException ex)
            {
                OutputManager.println(Launcher.OUT_COMMON, "Wrong number format (must be a long integer).");
            }
        }
    }
}
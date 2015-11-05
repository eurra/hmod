
package hmod.launcher.commands;

import hmod.launcher.LauncherException;
import hmod.launcher.CommandInfo;
import hmod.launcher.Command;
import hmod.launcher.Launcher;
import hmod.launcher.components.RandomData;
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
    private RandomData randomHandler;

    public void setRandomData(RandomData handler)
    {
        this.randomHandler = handler;
    }
    
    @Override
    public void executeCommand(String[] args) throws LauncherException
    {
        if(args.length < 1)
        {
            OutputManager.println(Launcher.OUT_COMMON, "Next seed: " + randomHandler.getSeed() + ".");
        }
        else
        {
            try
            {
                long seed = Long.parseLong(args[0]);
                randomHandler.setSeed(seed);
                OutputManager.println(Launcher.OUT_COMMON, "New seed configurated: " + seed + ".");
            }
            catch(NumberFormatException ex)
            {
                OutputManager.println(Launcher.OUT_COMMON, "Wrong number format (must be a long integer).");
            }
        }
    }
}
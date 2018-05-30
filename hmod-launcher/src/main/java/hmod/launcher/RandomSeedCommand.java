
package hmod.launcher;

import optefx.util.output.OutputManager;

/**
 * Implements the 'random_seed' launcher command
 * @author Enrique Urra C.
 */
@CommandInfo(
    word="randomSeed",
    usage="randomSeed [seed]",
    description="Sets or gets the random seed to be used in the next algorithm "
        + "run.\n"
        + "[seed] The seed to set. Fi this argument is not provided, the next "
        + "seed to be used will be reported."
)
class RandomSeedCommand extends Command
{    
    private final RandomSeed randomHandler;

    public RandomSeedCommand(RandomSeed randomHandler)
    {
        this.randomHandler = randomHandler;
    }
    
    @Override
    public void executeCommand(CommandArgs args) throws LauncherException
    {
        if(args.getCount() < 1)
        {
            OutputManager.println(Launcher.OUT_COMMON, "Next seed: " + randomHandler.getCurrent()+ ".");
        }
        else
        {
            try
            {
                long seed = args.getArgAs(0, Number.class).longValue();
                randomHandler.setCurrent(seed);
                OutputManager.println(Launcher.OUT_COMMON, "New seed configurated: " + seed + ".");
            }
            catch(NumberFormatException ex)
            {
                OutputManager.println(Launcher.OUT_COMMON, "Wrong number format (must be a long integer).");
            }
        }
    }
}

package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
class RandomProcessor implements TextVariableProcessor
{
    private final RandomSeed randomHandler;

    public RandomProcessor(RandomSeed randomHandler)
    {
        this.randomHandler = randomHandler;
    }
    
    @Override
    public String process(String input) throws LauncherException
    {        
        if(input.equals("RAND_SEED"))
            return Long.toString(randomHandler.getCurrent());
        
        return null;
    }
}

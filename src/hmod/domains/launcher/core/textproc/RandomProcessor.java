
package hmod.domains.launcher.core.textproc;

import hmod.domains.launcher.core.LauncherException;
import hmod.domains.launcher.core.TextVariableProcessor;
import hmod.domains.launcher.core.ac.RandomHandler;

/**
 *
 * @author Enrique Urra C.
 */
public class RandomProcessor implements TextVariableProcessor
{
    private RandomHandler randomHandler;

    public void setRandomHandler(RandomHandler randomHandler)
    {
        this.randomHandler = randomHandler;
    } 
    
    @Override
    public String process(String input) throws LauncherException
    {        
        if(input.equals("RAND_SEED"))
            return Long.toString(randomHandler.getCurrentSeed());
        
        return null;
    }
}

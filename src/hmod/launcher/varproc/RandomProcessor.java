
package hmod.launcher.varproc;

import hmod.launcher.LauncherException;
import hmod.launcher.VariableProcessor;
import hmod.launcher.components.RandomData;

/**
 *
 * @author Enrique Urra C.
 */
public class RandomProcessor implements VariableProcessor
{
    private RandomData randomHandler;

    public void setRandomData(RandomData handler)
    {
        this.randomHandler = handler;
    }
    
    @Override
    public String process(String input) throws LauncherException
    {        
        return input.replace("RAND_SEED", Long.toString(randomHandler.getSeed()));
    }
}

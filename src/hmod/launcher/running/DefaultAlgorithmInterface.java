
package hmod.launcher.running;

import hmod.core.Algorithm;
import optefx.util.output.SystemOutputConfigBuilder;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultAlgorithmInterface implements AlgorithmInterface
{
    @Override
    public void configOutputs(SystemOutputConfigBuilder outputConfigBuilder)
    {
    }
    
    @Override
    public void init(Algorithm algorithm, Thread algorithmThread)
    {        
        try
        {
            if(algorithmThread != null)
                algorithmThread.join();
        }
        catch(InterruptedException ex)
        {
        }
    }    
}

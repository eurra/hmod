
package hmod.domains.launcher.core.running;

import hmod.core.Algorithm;
import optefx.util.output.OutputConfigBuilder;

/**
 *
 * @author Enrique Urra C.
 */
class DefaultAlgorithmInterface implements AlgorithmInterface
{
    @Override
    public void configOutputs(OutputConfigBuilder outputConfigBuilder)
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

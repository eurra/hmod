
package hmod.domains.launcher.graph;

import hmod.domains.launcher.core.Command;
import hmod.domains.launcher.core.ac.AlgorithmLoaderHandler;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class AlgorithmScriptLoaderCommand extends Command
{
    private AlgorithmLoaderHandler algorithmLoaderHandler;

    public void setAlgorithmLoaderHandler(AlgorithmLoaderHandler algorithmLoaderHandler)
    {
        this.algorithmLoaderHandler = algorithmLoaderHandler;
    }

    public AlgorithmLoaderHandler getAlgorithmLoaderHandler()
    {
        if(algorithmLoaderHandler == null)
            throw new NullPointerException("Null algorithm loader handler");
        
        return algorithmLoaderHandler;
    }

    @Override
    public boolean isEnabled()
    {
        if(algorithmLoaderHandler == null)
            throw new NullPointerException("Null algorithm loader handler");
        
        return algorithmLoaderHandler.getCurrentLoader() instanceof AlgorithmScriptLoader;
    }
}


package hmod.launcher.running;

import hmod.core.Algorithm;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class AlgorithmRunnerFactory
{
    private RunnerInfo info;
    
    public AlgorithmRunnerFactory()
    {
        info = this.getClass().getAnnotation(RunnerInfo.class);
    }
    
    public String getId()
    {
        if(info != null)
            return info.id();
        
        return null;
    }
    
    public String getDescription()
    {
        if(info != null)
            return info.description();
        
        return null;
    }
    
    public abstract AlgorithmRunner createRunner();
}

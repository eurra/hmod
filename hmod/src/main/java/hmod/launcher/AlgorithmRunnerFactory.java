
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public abstract class AlgorithmRunnerFactory
{
    private final RunnerInfo info;
    
    public AlgorithmRunnerFactory()
    {
        info = this.getClass().getAnnotation(RunnerInfo.class);
    }

    public final RunnerInfo getInfo()
    {
        return info;
    }
    
    public abstract AlgorithmRunner createRunner();
}

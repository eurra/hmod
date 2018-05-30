
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
@RunnerInfo(
    id = "default",
    description = "Default algorithm runner. Provides information regarding the"
        + " algorithm delay through the system output."
)
public class DefaultAlgorithmRunnerFactory extends AlgorithmRunnerFactory
{    
    @Override
    public AlgorithmRunner createRunner()
    {
        DefaultAlgorithmRunner runner = new DefaultAlgorithmRunner();        
        return runner;
    }
}

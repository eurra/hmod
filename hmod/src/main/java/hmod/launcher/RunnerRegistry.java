
package hmod.launcher;

/**
 *
 * @author Enrique Urra C.
 */
public interface RunnerRegistry
{
    RunnerRegistry addFactories(AlgorithmRunnerFactory... factories);
    RunnerRegistry addFactory(AlgorithmRunnerFactory factory);
    boolean runnerExists(String id);
}


package hmod.launcher;

import hmod.core.Procedure;
import optefx.util.output.OutputConfig;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmInterface
{
    public Runnable configure(Procedure algorithm, OutputConfig outputConfigBuilder);
    void start(Thread launcherThread);
}
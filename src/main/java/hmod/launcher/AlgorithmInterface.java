
package hmod.launcher;

import hmod.core.Statement;
import optefx.util.output.OutputConfig;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmInterface
{
    public Runnable configure(Statement algorithm, OutputConfig outputConfigBuilder);
    void start(Thread launcherThread);
}
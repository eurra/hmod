
package hmod.launcher.running;

import hmod.core.Algorithm;
import optefx.util.output.SystemOutputConfigBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmInterface
{
    public void configOutputs(SystemOutputConfigBuilder outputConfigBuilder);
    public void init(Algorithm algorithm, Thread algorithmThread);
}
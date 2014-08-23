
package hmod.domains.launcher.core.running;

import hmod.core.Algorithm;
import optefx.util.output.OutputConfigBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface AlgorithmInterface
{
    public void configOutputs(OutputConfigBuilder outputConfigBuilder);
    public void init(Algorithm algorithm, Thread algorithmThread);
}
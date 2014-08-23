
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;
import hmod.domains.launcher.core.CommandBuffer;
import hmod.domains.launcher.core.VariableCollection;

/**
 *
 * @author Enrique Urra C.
 */
public interface CommandBufferHandler extends DataHandler
{
    CommandBuffer pushBuffer(String source);
    boolean popBuffer();
    boolean existsCurrentBuffer();
    CommandBuffer getCurrentBuffer();
    VariableCollection getVariableCollectionFromBuffer();
}

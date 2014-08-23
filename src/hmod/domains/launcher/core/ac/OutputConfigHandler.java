

package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;
import optefx.util.output.OutputConfigBuilder;

/**
 *
 * @author Enrique Urra C.
 */
public interface OutputConfigHandler extends DataHandler
{
    void addFileOutput(String id, String path, boolean append);
    void addSystemOutput(String id);
    void restartConfig();
    OutputConfigBuilder createConfigBuilder();
}

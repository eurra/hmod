
package hmod.domains.launcher.core.ac;

import hmod.core.DataHandler;

/**
 *
 * @author Enrique Urra C.
 */
public interface WindowsHandler extends DataHandler
{    
    void enableAutoClose();
    void disableAutoClose();
    boolean isAutoCloseEnabled();
}

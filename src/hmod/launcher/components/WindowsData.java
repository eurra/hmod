
package hmod.launcher.components;

import hmod.core.DataInterface;

/**
 *
 * @author Enrique Urra C.
 */
public interface WindowsData extends DataInterface
{    
    void setWindowAutoClose(boolean debug);
    boolean getWindowAutoClose();
}

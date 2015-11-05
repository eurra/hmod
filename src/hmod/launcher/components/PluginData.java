
package hmod.launcher.components;

import hmod.core.DataInterface;
import hmod.launcher.Plugin;

/**
 *
 * @author Enrique Urra C.
 */
public interface PluginData extends DataInterface
{    
    void setPlugins(Plugin[] plugins);
    Plugin[] getPlugins();
}

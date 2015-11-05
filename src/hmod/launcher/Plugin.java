
package hmod.launcher;

/**
 * Defines a general type for the console command's plugins.
 * @author Enrique Urra C.
 */
public interface Plugin
{
    /**
     * Executes the plugin.
     */
    void runPlugin() throws LauncherException;
}
